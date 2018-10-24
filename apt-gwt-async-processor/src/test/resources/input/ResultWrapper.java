package net.omikron.apt.gwt;

public class ResultWrapper<T> {

	private final T                value;
	private final RuntimeException error;

	private ResultWrapper(final T value, final RuntimeException error) {
		this.value = value;
		this.error = error;
	}

	public T tryGet() {
		if (error != null) throw error;
		return value;
	}

	public static <T> ResultWrapper<T> success(final T value) {
		return new ResultWrapper<>(value, null);
	}

	public static <T> ResultWrapper<T> failure(final RuntimeException error) {
		return new ResultWrapper<>(null, error);
	}
}