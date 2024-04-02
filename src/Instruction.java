import com.alibaba.fastjson.JSON;

public class Instruction
{
	private int code;
	private boolean status;
	private String body;

	Instruction() {}

	Instruction(UserM user)
	{
		code = Const.SIGN_IN;
		body = JSON.toJSONString(user);
	}

	public int getCode()
	{
		return code;
	}

	public void setCode(int code)
	{
		this.code = code;
	}

	public boolean getStatus()
	{
		return status;
	}

	public void setStatus(boolean status)
	{
		this.status = status;
	}

	public String getBody()
	{
		return body;
	}

	public void setBody(String body)
	{
		this.body = body;
	}
}