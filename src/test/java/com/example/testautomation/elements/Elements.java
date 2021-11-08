package com.example.testautomation.elements;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public enum Elements {
    LOGIN_USERNAME("#login-form-username"),
    LOGIN_PASSWORD("#login-form-password"),
    LOGIN_BUTTON("#login-form-submit"),
    OTHER_LOGIN_BUTTON("#login"),
    PROFILE_BUTTON("#header-details-user-fullname"),
    LOGOUT_OPTION("#log_out"),
    PROFILE_OPTION("#view_profile"),
    PROFILE_USERNAME("#up-d-username");

    private String selector;

    Elements(String selector) {
        this.selector = selector;
    }

    public SelenideElement getElement() {
        return $(selector);
    }
}
