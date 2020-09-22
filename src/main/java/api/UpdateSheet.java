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
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

public class UpdateSheet {
  private static Sheets sheetsService;
  private static String APPLICATION_NAME = "DummyApp";
  private static String SPREADSHEET_ID = "1BoXHu683YywbVb400FOS5Emn3GSOr-VUH54fmwhk6Jo";

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

  public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
    Credential credential = authorize();
    return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
            JacksonFactory.getDefaultInstance(), credential)
            .setApplicationName(APPLICATION_NAME)
            .build();
  }

  public static void main(String[] args) throws IOException, GeneralSecurityException {
    sheetsService = getSheetsService();
    //readSpreadsheet();
    writeSpreadsheet(args);
  }

  private static void readSpreadsheet() throws IOException {
    String range = "Test_Result!A1:C1";

    ValueRange response = sheetsService.spreadsheets().values()
            .get(SPREADSHEET_ID,range)
            .execute();

    List<List<Object>> values = response.getValues();

    if (values == null || values.isEmpty()) {
      System.out.println("No data found");
    } else {
      for (List row : values) {
        System.out.printf("%s, %s, %s\n", row.get(2),row.get(1),row.get(0));
      }
    }


  }

  private static void writeSpreadsheet(String[] args) throws IOException {
    ValueRange appendBody = new ValueRange()
            .setValues(Arrays.asList(
                    Arrays.asList(args[0],args[1],args[2])
            ));

    AppendValuesResponse appendResult = sheetsService.spreadsheets().values()
            .append(SPREADSHEET_ID,"report", appendBody)
            .setValueInputOption("USER_ENTERED")
            .setInsertDataOption("INSERT_ROWS")
            .setIncludeValuesInResponse(true)
            .execute();
  }
}
