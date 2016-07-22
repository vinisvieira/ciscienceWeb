package br.com.ciscience.controll.rest.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.annotation.security.PermitAll;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;

import br.com.ciscience.util.Constants;

@Path("/datafile")
public class DataFileRestService {

	@Context
	private HttpServletRequest myHttpServletRequest;

	@Context
	private HttpServletResponse myHttpServletResponse;

	@GET
	@Path("/{filename}")
	@PermitAll
	public void getFile(@PathParam("filename") String fileName) {

		try {

			myHttpServletResponse.setContentType("image/jpeg");

			ServletOutputStream out = myHttpServletResponse.getOutputStream();

			FileInputStream fin = new FileInputStream(
					System.getProperty(Constants.CATALINA_BASE) + Constants.UPLOAD_PATH + fileName);

			BufferedInputStream bin = new BufferedInputStream(fin);
			BufferedOutputStream bout = new BufferedOutputStream(out);
			int ch = 0;
			while ((ch = bin.read()) != -1) {
				bout.write(ch);
			}

			bin.close();
			fin.close();
			bout.close();
			out.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
