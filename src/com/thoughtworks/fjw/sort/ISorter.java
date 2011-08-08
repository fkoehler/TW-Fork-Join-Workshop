package com.thoughtworks.fjw.sort;

import java.util.List;

public interface ISorter<T> {
	public List<T> sort(List<T> list);
}
