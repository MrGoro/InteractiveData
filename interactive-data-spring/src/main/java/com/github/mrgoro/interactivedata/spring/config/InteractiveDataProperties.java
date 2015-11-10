package com.github.mrgoro.interactivedata.spring.config;

/**
 * Interactive Data Configurations Options
 *
 * @author Philipp Sch&uuml;rmann
 */
public class InteractiveDataProperties {

    private String path = "com.github.mrgoro.interactivedata";

    private Swagger swagger = new Swagger();

    public static class Swagger {

        private String title = "Interactive Data API";

        private String description = "Interactive Data API Documentation";

        private String version = "0.0.1";

        private String termsOfServiceUrl;

        private String contact;

        private String license = "MIT License";

        private String licenseUrl = "http://opensource.org/licenses/MIT";

        private String basePath = "api";

        private boolean https = true;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getTermsOfServiceUrl() {
            return termsOfServiceUrl;
        }

        public void setTermsOfServiceUrl(String termsOfServiceUrl) {
            this.termsOfServiceUrl = termsOfServiceUrl;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public String getLicense() {
            return license;
        }

        public void setLicense(String license) {
            this.license = license;
        }

        public String getLicenseUrl() {
            return licenseUrl;
        }

        public void setLicenseUrl(String licenseUrl) {
            this.licenseUrl = licenseUrl;
        }

        public String getBasePath() {
            return basePath;
        }

        public void setBasePath(String basePath) {
            this.basePath = basePath;
        }

        public boolean isHttps() {
            return https;
        }

        public void setHttps(boolean https) {
            this.https = https;
        }
    }

    public String getPath() {
        return path;
    }

    public Swagger getSwagger() {
        return swagger;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSwagger(Swagger swagger) {
        this.swagger = swagger;
    }
}