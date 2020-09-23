package api;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Arrays;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

/**
 * This class will be excecuted once the programmer pushes his/her codes into Git. The
 * continuous integration by Travis will be automatically triggered and this program
 * will be excecuted after the machine performs tests.
 *
 * The program will update the test report to Google spreadsheet automatically using
 * Google API. The test report consist of first name of the author, last name of the
 * author and also the test result (either successful or fail).
 */
public class UpdateSheet {
  private static Sheets sheetsService;
  private static String APPLICATION_NAME = "DummyApp";
  private static String SPREADSHEET_ID = "1BoXHu683YywbVb400FOS5Emn3GSOr-VUH54fmwhk6Jo";

  private static final int RELEVANT_ARGUMENT_LENGTH = 3;
  private static final int FIRST_NAME_INDEX = 0;
  private static final int LAST_NAME_INDEX = 1;
  private static final int RESULT_INDEX = 2;

  /**
   * The main program access the test report spreadsheet file from Google Spreadsheet
   * and it updates the report.
   * @param args args is passed arguments from yml-file.
   * @throws IOException
   * @throws GeneralSecurityException
   */
  public static void main(String[] args) throws IOException, GeneralSecurityException {
    sheetsService = getSheetsService();
    args = fixArgs(args);
    writeSpreadsheet(args);
  }

  /**
   * This method authorizes the program before it accesses a spreadsheet file.
   * @throws IOException
   * @throws GeneralSecurityException
   */
  private static Credential authorize() throws IOException, GeneralSecurityException {
    InputStream in = UpdateSheet.class.getResourceAsStream("/credentials.json");
    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
            JacksonFactory.getDefaultInstance(),new InputStreamReader(in)
    );

    List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);

    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
            GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(),
            clientSecrets, scopes)
            .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
            .setAccessType("offline")
            .build();
    Credential credential = new AuthorizationCodeInstalledApp(
            flow, new LocalServerReceiver())
            .authorize("user");

    return credential;
  }

  /**
   * This method access the needed spreadsheet from Google spreadsheet.
   * @throws IOException
   * @throws GeneralSecurityException
   */
  private static Sheets getSheetsService() throws IOException, GeneralSecurityException {
    Credential credential = authorize();
    return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
            JacksonFactory.getDefaultInstance(), credential)
            .setApplicationName(APPLICATION_NAME)
            .build();
  }

  /**
   * This method will extract the necessary data for test report. The relevant
   * data should be the first name of the author, last name of the author, and
   * the test result (either successful or fail).
   * @param args is passed arguments from yml-file.
   * @return relevant data for test report.
   */
  private static String[] fixArgs(String[] args) {
    if (hasLastNameOnly(args)) {
      args = addEmptyFirstName(args);
    } else {
      args = getFirstAndLastNameOnly(args);
    }
    return args;
  }

  /**
   * This method writes the necessary data into the test report in
   * Google spreadsheet.
   * @param args is the necessary data that need to be written in
   *             the test report.
   * @throws IOException
   */
  private static void writeSpreadsheet(String[] args) throws IOException {
    ValueRange appendBody = new ValueRange()
            .setValues(Arrays.asList(
                    Arrays.asList(args[0],args[1],args[2])
            ));

    sheetsService.spreadsheets().values()
            .append(SPREADSHEET_ID,"report", appendBody)
            .setValueInputOption("USER_ENTERED")
            .setInsertDataOption("INSERT_ROWS")
            .setIncludeValuesInResponse(true)
            .execute();
  }

  private static String[] addEmptyFirstName(String[] args) {
    String[] fixedArgs = new String[RELEVANT_ARGUMENT_LENGTH];
    fixedArgs[FIRST_NAME_INDEX] = "";
    fixedArgs[LAST_NAME_INDEX] = args[0];
    fixedArgs[RESULT_INDEX] = args[args.length-1];
    return fixedArgs;
  }

  private static String[] getFirstAndLastNameOnly(String[] args) {
    String[] fixedArgs = new String[RELEVANT_ARGUMENT_LENGTH];
    fixedArgs[FIRST_NAME_INDEX] = args[0];
    fixedArgs[LAST_NAME_INDEX] = args[args.length-2];
    fixedArgs[RESULT_INDEX] = args[args.length-1];
    return fixedArgs;
  }

  private static boolean hasLastNameOnly(String[] args) {
    return args.length == 2;
  }
}
