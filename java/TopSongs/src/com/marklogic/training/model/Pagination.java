package com.marklogic.training.model;

public class Pagination {
	
	public Pagination() {
	}
	
	public Pagination(long start, long length, long total, long last, long end,
			long next, long previous, long currpage, long totpages,
			long rangestart, long rangeend) 
	{
		this.start = start;
		this.length = length;
		this.total = total;
		this.last = last;
		this.end = end;
		this.next = next;
		this.previous = previous;
		this.currpage = currpage;
		this.totpages = totpages;
		this.rangestart = rangestart;
		this.rangeend = rangeend;
	}

	private long start;
	private long length;
	private long total;
	private long last;
	private long end;
	private long next;
	private long previous;
	private long currpage;
	private long totpages;
	private long rangestart;
	private long rangeend;

	public long getStart() {
		return start;
	}
	public void setStart(long start) {
		this.start = start;
	}
	public long getLength() {
		return length;
	}
	public void setLength(long length) {
		this.length = length;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public long getLast() {
		return last;
	}
	public void setLast(long last) {
		this.last = last;
	}
	public long getEnd() {
		return end;
	}
	public void setEnd(long end) {
		this.end = end;
	}
	public long getNext() {
		return next;
	}
	public void setNext(long next) {
		this.next = next;
	}
	public long getPrevious() {
		return previous;
	}
	public void setPrevious(long previous) {
		this.previous = previous;
	}
	public long getCurrpage() {
		return currpage;
	}
	public void setCurrpage(long currpage) {
		this.currpage = currpage;
	}
	public long getTotpages() {
		return totpages;
	}
	public void setTotpages(long totpages) {
		this.totpages = totpages;
	}
	public long getRangestart() {
		return rangestart;
	}
	public void setRangestart(long rangestart) {
		this.rangestart = rangestart;
	}
	public long getRangeend() {
		return rangeend;
	}
	public void setRangeend(long rangeend) {
		this.rangeend = rangeend;
	}

	@Override
	public String toString() {
		return "Pagination [start=" + start + ", length=" + length + ", total="
				+ total + ", last=" + last + ", end=" + end + ", next=" + next
				+ ", previous=" + previous + ", currpage=" + currpage
				+ ", totpages=" + totpages + ", rangestart=" + rangestart
				+ ", rangeend=" + rangeend + "]";
	}
	
	
}
