package io.github.schneiderlin.fetch.feedDemo.database;

import lombok.Data;

@Data
public class UserTable {
    private String id;
    private String name;
    private String description;
    private String gender;
    private String email;
}
