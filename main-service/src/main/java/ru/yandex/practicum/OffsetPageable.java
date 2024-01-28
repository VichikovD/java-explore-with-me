package ru.yandex.practicum;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class OffsetPageable implements Pageable {
    private final int offset;
    private final int limit;
    private final Sort sort;

    public OffsetPageable(int offset, int limit, Sort sort) {
        if (offset < 0) {
            throw new IllegalArgumentException("Offset must not be less than 0");
        }
        if (limit < 1) {
            throw new IllegalArgumentException("Limit must not be less than 1");
        }
        if (sort == null) {
            throw new IllegalArgumentException("Sort must not be null");
        }
        this.offset = offset;
        this.limit = limit;
        this.sort = sort;
    }

    @Override
    public int getPageNumber() {
        return offset / limit;
    }

    @Override
    public int getPageSize() {
        return this.limit;
    }

    @Override
    public long getOffset() {
        return this.offset;
    }

    @Override
    public Sort getSort() {
        return this.sort;
    }

    @Override
    public Pageable next() {
        return new OffsetPageable(this.offset + this.limit, this.limit, this.sort);
    }

    @Override
    public boolean hasPrevious() {
        return this.offset > this.limit;
    }

    public Pageable previous() {
        return this.hasPrevious() ? new OffsetPageable((this.offset - this.limit), this.getPageSize(), this.getSort()) : this;
    }

    @Override
    public Pageable previousOrFirst() {
        return this.hasPrevious() ? this.previous() : this.first();
    }

    @Override
    public Pageable first() {
        return new OffsetPageable(0, this.limit, this.sort);
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return new OffsetPageable(pageNumber * this.limit, this.limit, this.sort);
    }
}
