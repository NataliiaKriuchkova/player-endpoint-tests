package com.player.model.request;

public class UpdatePlayerRequest {

    private Integer age;
    private String gender;
    private String login;
    private String password;
    private String screenName;

    private UpdatePlayerRequest() {}

    public Integer getAge() { return age; }
    public String getGender() { return gender; }
    public String getLogin() { return login; }
    public String getPassword() { return password; }
    public String getScreenName() { return screenName; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer age;
        private String gender;
        private String login;
        private String password;
        private String screenName;

        public Builder age(Integer age) {
            this.age = age;
            return this;
        }

        public Builder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder login(String login) {
            this.login = login;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder screenName(String screenName) {
            this.screenName = screenName;
            return this;
        }

        public UpdatePlayerRequest build() {
            UpdatePlayerRequest request = new UpdatePlayerRequest();
            request.age = this.age;
            request.gender = this.gender;
            request.login = this.login;
            request.password = this.password;
            request.screenName = this.screenName;
            return request;
        }
    }
}
