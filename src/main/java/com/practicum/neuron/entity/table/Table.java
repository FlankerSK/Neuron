package com.practicum.neuron.entity.table;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Document("tables")
public class Table {
    // id
    @Id
    private String id;

    // 表标题
    @Field("title")
    private String tittle;

    // 作者
    @Field("author")
    private String author;

    // 更新时间，默认为创建时间
    @Default
    @Field("update_date")
    private Date updateDate = new Date();

    // 问题列表，一开始为空
    @Default
    @Field("questions")
    private List<org.bson.Document> questions = new ArrayList<>();

    // 表状态, 一开始为未发布状态
    @Default
    @Field("status")
    private TableStatus status = TableStatus.UNPUBLISH;
}
