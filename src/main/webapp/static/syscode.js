function jczState(code)
{
	var name="营业";
	
	if (code=="1")
	{
		name="营业";
	} else if (code=="2")
	{
		name="停业";
	}else if (code=="3")
	{
		name="整改";
	}else if (code=="4")
	{
		name="停业整顿";
	}else if (code=="5")
	{
		name="歇业";
	}else if (code=="6")
	{
		name="注销";
	}else if (code=="7")
	{
		name="其它";
	}
	
	return name;
}

function jczCheckFlag(code)
{
	var name="未提交";
	
	if (code=="1")
	{
		name="待审核";
	} else if (code=="2")
	{
		name="检测工委审核同意";
	}else if (code=="3")
	{
		name="检测工委审核不同意";
	}else if (code=="4")
	{
		name="专家审核同意";
	}else if (code=="5")
	{
		name="专家审核不同意";
	}

	return name;
}
