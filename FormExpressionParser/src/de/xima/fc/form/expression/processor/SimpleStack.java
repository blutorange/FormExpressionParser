package de.xima.fc.form.expression.processor;

import java.util.Arrays;

public class SimpleStack<T> {
	private T[] array;
	private int pos = -1;
	public SimpleStack() {
		this(32);
	}
	@SuppressWarnings("unchecked")
	public SimpleStack(final int initialSize) {
		array = (T[])(new Object[initialSize]);
	}
	public void push(final T object) {
		if (pos >= array.length) {
			array = Arrays.copyOf(array, 2*pos+1);
		}
		array[++pos] = object;
	}
	public T pop() {
		return array[pos--];
	}
	public T peek() {
		return array[pos];
	}
	public T peek2() {
		return array[pos-1];
	}
	public void clear() {
		pos = -1;
	}
	public boolean isEmpty() {
		return pos == -1;
	}
	public int size() {
		return pos+1;
	}
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (int i = 0; i <= pos; ++i)
			sb.append(array[i].toString()).append(',');
		sb.setLength(sb.length()-1);
		sb.append(']');
		return sb.toString();
	}
}
