import java.io.IOException;


public class InvalidFileException extends IOException
{
	public InvalidFileException(String message)
	{
		super(message);
	}
}
