package com.fingerbook.rest.domain.util;

import java.util.ArrayList;
import java.util.List;

public class Page<T> extends ArrayList<T>{
	
	private static final long serialVersionUID = 1L;

	private Integer totalResultSize;
	private Integer pageNumber;
	private Integer pagesSize;

	public Page() {
	    super();
	}
	
	@SuppressWarnings("unchecked")
	public Page(Page<Object> page) {
	    super();
	    for (Object o : page) {
	        this.add((T)o);
	    }
	    setTotalResultSize(page.getTotalResultSize());
	    setPageNumber(page.getPageNumber());
	    setPagesSize(page.pagesSize);
	}
	
        @SuppressWarnings("unchecked")
        public Page(List collection, int first, int size) {
            this.addAll(0, collection);
            setTotalResultSize(collection.size());
            setPageNumber(first/size);
            setPagesSize(size);
        }

        public Integer getTotalResultSize() {
		return totalResultSize;
	}

	public void setTotalResultSize(Integer totalResultSize) {
		this.totalResultSize = totalResultSize;
	}
	
	public Integer getPageNumber() {
	    return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
	    this.pageNumber = pageNumber;
	}
	
	public void setPagesSize(Integer size) {
	    this.pagesSize = size;
	}
	
	public Integer getTotalPages() {
		Double cant=(Double)(getTotalResultSize() / (double)this.pagesSize);
		if(cant%1==0)
			return cant.intValue();
	    return (1 + cant.intValue());
	}
}
