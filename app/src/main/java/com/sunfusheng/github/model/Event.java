package com.sunfusheng.github.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author sunfusheng on 2018/5/7.
 */
@Entity
public class Event {

    @NonNull
    @PrimaryKey
    public String id;
    public String type;
    @Embedded
    public ActorEntity actor;
    @Embedded
    public RepoEntity repo;
    @Embedded
    public PayloadEntity payload;
    @SerializedName("public")
    public boolean publicX;
    public String created_at;

    public static class ActorEntity {

        @ColumnInfo(name = "actor_id")
        public int id;
        public String login;
        public String display_login;
        public String gravatar_id;
        @ColumnInfo(name = "actor_url")
        public String url;
        public String avatar_url;
    }

    public static class RepoEntity {

        @ColumnInfo(name = "repo_id")
        public int id;
        public String name;
        @ColumnInfo(name = "repo_url")
        public String url;
    }

    public static class PayloadEntity {

        public long push_id;
        public int size;
        public int distinct_size;
        public String ref;
        public String head;
        public String before;
        @Embedded
        public CommitsEntity commit;
        @Ignore
        public List<CommitsEntity> commits;

        public static class CommitsEntity {

            public String sha;
            @Embedded
            public AuthorEntity author;
            public String message;
            public boolean distinct;
            @ColumnInfo(name = "commits_url")
            public String url;

            public static class AuthorEntity {

                public String email;
                @ColumnInfo(name = "author_name")
                public String name;

            }
        }
    }
}
