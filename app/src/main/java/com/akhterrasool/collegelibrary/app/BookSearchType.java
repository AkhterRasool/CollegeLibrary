package com.akhterrasool.collegelibrary.app;


public enum BookSearchType {

    TITLE,
    AUTHOR;

    @Override
    public String toString() {
        return this.name().toUpperCase();
    }
}
