package app.youngmon.surl.exception;

@SuppressWarnings("checkstyle:SummaryJavadoc")
public class BadRequestException extends RuntimeException {
  public BadRequestException(String msg) {
    super(msg);
  }
}