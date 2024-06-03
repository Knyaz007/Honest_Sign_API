package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.locks.ReentrantLock;

public class CrptApi {
    private final String apiUrl;
    private final RateLimiter rateLimiter;
    private final ReentrantLock lock;
    private final ObjectMapper objectMapper;

    public CrptApi(String apiUrl, double requestsPerSecond) {
        this.apiUrl = apiUrl;
        this.rateLimiter = RateLimiter.create(requestsPerSecond);
        this.lock = new ReentrantLock();
        this.objectMapper = new ObjectMapper();
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public RateLimiter getRateLimiter() {
        return rateLimiter;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public String createDocument(Document document, String signature) throws IOException {
        rateLimiter.acquire();
        lock.lock();
        try {
            URL url = new URL(apiUrl + "/api/v3/lk/documents/create");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Signature", signature);
            connection.setDoOutput(true);

            String jsonInputString = objectMapper.writeValueAsString(document);
            System.out.println("Request Body:");
            System.out.println(jsonInputString);
            try (OutputStream os = connection.getOutputStream()) {
                System.out.println("Request Body:");
                System.out.println(jsonInputString);

                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuilder response = new StringBuilder();
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    String responseLine;
                    while ((responseLine = in.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                }
                return response.toString();
            } else {
                throw new IOException("Server returned non-OK status: " + responseCode);
            }
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        try {
            CrptApi client = new CrptApi("https://ismp.crpt.ru", 1.0); // 1 request per second

            CrptApi.Description description = new CrptApi.Description();
            description.setParticipantInn("string");

            CrptApi.Product product = new CrptApi.Product();
            product.setCertificateDocument("string");
            product.setCertificateDocumentDate("2020-01-23");
            product.setCertificateDocumentNumber("string");
            product.setOwnerInn("string");
            product.setProducerInn("string");
            product.setProductionDate("2020-01-23");
            product.setTnvedCode("string");
            product.setUitCode("string");
            product.setUituCode("string");

            CrptApi.Document document = new CrptApi.Document();
            document.setDescription(description);
            document.setDocId("string");
            document.setDocStatus("string");
            document.setDocType("LP_INTRODUCE_GOODS");
            document.setImportRequest(true);
            document.setOwnerInn("string");
            document.setParticipantInn("string");
            document.setProducerInn("string");
            document.setProductionDate("2020-01-23");
            document.setProductionType("string");
            document.setProducts(new CrptApi.Product[]{product});
            document.setRegDate("2020-01-23");
            document.setRegNumber("string");

            String signature = "your_signature_here";
            String response = client.createDocument(document, signature);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static class Document {
        private Description description;
        private String docId;
        private String docStatus;
        private String docType;
        private boolean importRequest;
        private String ownerInn;
        private String participantInn;
        private String producerInn;
        private String productionDate;
        private String productionType;
        private Product[] products;
        private String regDate;
        private String regNumber;

        // Getters and Setters
        public Description getDescription() {
            return description;
        }

        public void setDescription(Description description) {
            this.description = description;
        }

        public String getDocId() {
            return docId;
        }

        public void setDocId(String docId) {
            this.docId = docId;
        }

        public String getDocStatus() {
            return docStatus;
        }

        public void setDocStatus(String docStatus) {
            this.docStatus = docStatus;
        }

        public String getDocType() {
            return docType;
        }

        public void setDocType(String docType) {
            this.docType = docType;
        }

        public boolean isImportRequest() {
            return importRequest;
        }

        public void setImportRequest(boolean importRequest) {
            this.importRequest = importRequest;
        }

        public String getOwnerInn() {
            return ownerInn;
        }

        public void setOwnerInn(String ownerInn) {
            this.ownerInn = ownerInn;
        }

        public String getParticipantInn() {
            return participantInn;
        }

        public void setParticipantInn(String participantInn) {
            this.participantInn = participantInn;
        }

        public String getProducerInn() {
            return producerInn;
        }

        public void setProducerInn(String producerInn) {
            this.producerInn = producerInn;
        }

        public String getProductionDate() {
            return productionDate;
        }

        public void setProductionDate(String productionDate) {
            this.productionDate = productionDate;
        }

        public String getProductionType() {
            return productionType;
        }

        public void setProductionType(String productionType) {
            this.productionType = productionType;
        }

        public Product[] getProducts() {
            return products;
        }

        public void setProducts(Product[] products) {
            this.products = products;
        }

        public String getRegDate() {
            return regDate;
        }

        public void setRegDate(String regDate) {
            this.regDate = regDate;
        }

        public String getRegNumber() {
            return regNumber;
        }

        public void setRegNumber(String regNumber) {
            this.regNumber = regNumber;
        }
    }

    public static class Product {
        private String certificateDocument;
        private String certificateDocumentDate;
        private String certificateDocumentNumber;
        private String ownerInn;
        private String producerInn;
        private String productionDate;
        private String tnvedCode;
        private String uitCode;
        private String uituCode;

        // Getters and Setters
        public String getCertificateDocument() {
            return certificateDocument;
        }

        public void setCertificateDocument(String certificateDocument) {
            this.certificateDocument = certificateDocument;
        }

        public String getCertificateDocumentDate() {
            return certificateDocumentDate;
        }

        public void setCertificateDocumentDate(String certificateDocumentDate) {
            this.certificateDocumentDate = certificateDocumentDate;
        }

        public String getCertificateDocumentNumber() {
            return certificateDocumentNumber;
        }

        public void setCertificateDocumentNumber(String certificateDocumentNumber) {
            this.certificateDocumentNumber = certificateDocumentNumber;
        }

        public String getOwnerInn() {
            return ownerInn;
        }

        public void setOwnerInn(String ownerInn) {
            this.ownerInn = ownerInn;
        }

        public String getProducerInn() {
            return producerInn;
        }

        public void setProducerInn(String producerInn) {
            this.producerInn = producerInn;
        }

        public String getProductionDate() {
            return productionDate;
        }

        public void setProductionDate(String productionDate) {
            this.productionDate = productionDate;
        }

        public String getTnvedCode() {
            return tnvedCode;
        }

        public void setTnvedCode(String tnvedCode) {
            this.tnvedCode = tnvedCode;
        }

        public String getUitCode() {
            return uitCode;
        }

        public void setUitCode(String uitCode) {
            this.uitCode = uitCode;
        }

        public String getUituCode() {
            return uituCode;
        }

        public void setUituCode(String uituCode) {
            this.uituCode = uituCode;
        }
    }




    public static class Description {
        private String participantInn;

        // Getter and Setter
        public String getParticipantInn() {
            return participantInn;
        }

        public void setParticipantInn(String participantInn) {
            this.participantInn = participantInn;
        }
    }

}
