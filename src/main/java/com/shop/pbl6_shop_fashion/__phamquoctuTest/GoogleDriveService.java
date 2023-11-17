package com.shop.pbl6_shop_fashion.__phamquoctuTest;

import com.shop.pbl6_shop_fashion.util.GoogleDriveUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class GoogleDriveService {
//    /**
//     * Application name.
//     */
//    private static final String APPLICATION_NAME = "PBL6 Google Driver API";
//    /**
//     * Global instance of the JSON factory.
//     */
//    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
//    /**
//     * Directory to store authorization tokens for this application.
//     */
//    private static final String TOKENS_DIRECTORY_PATH = "tokens";
//
//    /**
//     * Global instance of the scopes required by this quickstart.
//     * If modifying these scopes, delete your previously saved tokens/ folder.
//     */
//    private static final List<String> SCOPES =
//            Collections.singletonList(DriveScopes.DRIVE_FILE);
//    private static final String CREDENTIALS_FILE_PATH = "credentials.json";
//
//    /**
//     * Creates an authorized Credential object.
//     *
//     * @param HTTP_TRANSPORT The network HTTP Transport.
//     * @return An authorized Credential object.
//     * @throws IOException If the credentials.json file cannot be found.
//     */
//
//    // Biến toàn cục để sử dụng
//    private final NetHttpTransport HTTP_TRANSPORT;
//    private final Drive service;
//    public GoogleDriveService() throws GeneralSecurityException, IOException {
//        HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//        service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
//                .setApplicationName(APPLICATION_NAME)
//                .build();
//    }
//
//    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
//            throws IOException {
//        // Load client secrets.
//        InputStream in = DriveQuickstart.class.getClassLoader().getResourceAsStream(CREDENTIALS_FILE_PATH);
//        if (in == null) {
//            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
//        }
//        GoogleClientSecrets clientSecrets =
//                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
//
//        // Build flow and trigger user authorization request.
//        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
//                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
//                .setAccessType("offline")
//                .build();
//        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
//        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
//        //returns an authorized Credential object.
//        return credential;
//    }


    // ======================= service function =======================
    public String uploadFile(MultipartFile file) throws Exception {
//        File fileMetadata = new File();
//        System.out.println(file.getName());
//        fileMetadata.setName(file.getName());
//        fileMetadata.setParents(Collections.singletonList("1QzWRpFokX1pH0pwqVXSgru9DGYEUY0ar")); // Đặt thư mục muốn lưu file vào
//
//        FileContent mediaContent = new FileContent("application/octet-stream", file);
//
//        File uploadedFile = service.files().create(fileMetadata, mediaContent)
//                .setFields("id")
//                .execute();
//
//        System.out.println("File đã được tải lên với ID: " + uploadedFile.getId());

        return GoogleDriveUtils.uploadImage(file);
    }
}
