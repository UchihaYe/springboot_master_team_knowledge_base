package com.knowledge.common.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "分页返回结果")
public class PageResult<T> extends Result<List<T>> {

    @Schema(description = "当前页码")
    private Long current;

    @Schema(description = "每页大小")
    private Long size;

    @Schema(description = "总记录数")
    private Long total;

    @Schema(description = "总页数")
    private Long pages;

    public PageResult() {
        super();
    }

    public PageResult(Long current, Long size, Long total, List<T> records) {
        super(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), records);
        this.current = current;
        this.size = size;
        this.total = total;
        this.pages = (total + size - 1) / size;
    }

    public static <T> PageResult<T> success(Long current, Long size, Long total, List<T> records) {
        return new PageResult<>(current, size, total, records);
    }

    public static <T> PageResult<T> empty(Long current, Long size) {
        return new PageResult<>(current, size, 0L, List.of());
    }
}