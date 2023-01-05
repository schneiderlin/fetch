package io.github.schneiderlin.fetch.example.feedDemo.database;

import lombok.Data;

@Data
public class UserTable {
    private String id;
    private String name;
    private String description;
    private String gender;
    private String email;
}
