package com.nfdw.util;

import java.io.File;

public class PdfUtil {

	/**
	 * html转pdf
	 * 
	 * @param srcPath
	 *            html路径，可以是硬盘上的路径，也可以是网络路径
	 * @param destPath
	 *            pdf保存路径
	 * @return 转换成功返回true
	 */
	public static boolean convert(String getPdfPagePath, String pdfFilePath, String toolPath) {
		File file = new File(pdfFilePath);
		File parent = file.getParentFile();
		
		// 如果pdf保存路径不存在，则创建路径
		if (!parent.exists()) {
			parent.mkdirs();
		}
		
		StringBuilder cmd = new StringBuilder();
		cmd.append(toolPath);
		cmd.append(" ");
		cmd.append(" --outline-depth 4");
		cmd.append(" --outline ");
		cmd.append(" --margin-bottom 1cm");
		cmd.append(" --margin-top 1cm ");
		cmd.append(" --footer-spacing 0 ");
		cmd.append(getPdfPagePath);
		cmd.append(" ");
		cmd.append(pdfFilePath);
		boolean result = true;
		
		try {
			Process proc = Runtime.getRuntime().exec(cmd.toString());
			HtmlToPdfInterceptor error = new HtmlToPdfInterceptor(proc.getErrorStream());
			HtmlToPdfInterceptor output = new HtmlToPdfInterceptor(proc.getInputStream());
			error.start();
			output.start();
			proc.waitFor();
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}

}
