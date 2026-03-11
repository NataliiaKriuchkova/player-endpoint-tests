package com.player.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Player {

    private Long id;
    private String login;
    private String password;
    private String screenName;
    private String gender;
    private Integer age;
    private String role;

    @JsonCreator
    public Player(
            @JsonProperty("id") Long id,
            @JsonProperty("login") String login,
            @JsonProperty("password") String password,
            @JsonProperty("screenName") String screenName,
            @JsonProperty("gender") String gender,
            @JsonProperty("age") Integer age,
            @JsonProperty("role") String role
    ) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.screenName = screenName;
        this.gender = gender;
        this.age = age;
        this.role = role;
    }

    private Player() {}

    public Long getId() { return id; }
    public String getLogin() { return login; }
    public String getPassword() { return password; }
    public String getScreenName() { return screenName; }
    public String getGender() { return gender; }
    public Integer getAge() { return age; }
    public String getRole() { return role; }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return builder()
                .id(this.id)
                .login(this.login)
                .password(this.password)
                .screenName(this.screenName)
                .gender(this.gender)
                .age(this.age)
                .role(this.role);
    }

    public Player withId(Long id) {
        return toBuilder()
                .id(id)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof Player)) { return false; }
        Player player = (Player) o;
        return Objects.equals(id, player.id) &&
                Objects.equals(login, player.login) &&
                Objects.equals(password, player.password) &&
                Objects.equals(screenName, player.screenName) &&
                Objects.equals(gender, player.gender) &&
                Objects.equals(age, player.age) &&
                Objects.equals(role, player.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, screenName, gender, age, role);
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", screenName='" + screenName + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", role=" + role +
                '}';
    }

    public static class Builder {

        private Long id;
        private String login;
        private String password;
        private String screenName;
        private String gender;
        private Integer age;
        private String role;

        public Builder id(Long id) {
            this.id = id;
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

        public Builder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder age(Integer age) {
            this.age = age;
            return this;
        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public Player build() {
            return new Player(id, login, password, screenName, gender, age, role);
        }
    }
}
