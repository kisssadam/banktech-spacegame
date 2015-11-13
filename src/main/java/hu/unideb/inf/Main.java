package hu.unideb.inf;

import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;

import eu.loxon.centralcontrol.CentralControl;
import eu.loxon.centralcontrol.CentralControlServiceService;
import eu.loxon.centralcontrol.CommonResp;
import eu.loxon.centralcontrol.ObjectFactory;
import eu.loxon.centralcontrol.StartGameResponse;

public class Main {

	public static void main(String[] args) throws MalformedURLException {
		args = getTestArgsArray();

		CentralControl centralControl = getCentralControl(args);
		ObjectFactory objectFactory = new ObjectFactory();

		StartGameResponse startGameResponse = centralControl.startGame(objectFactory.createStartGameRequest());

		CommonResp commonResp = startGameResponse.getResult();
		System.out.println(commonResp);

		System.out.println(startGameResponse.getSize());
		System.out.println(startGameResponse.getUnits());
	}

	private static final String[] getTestArgsArray() {
		String wsdlLocation = "http://javachallenge.loxon.hu:8443/engine/CentralControl?wsdl";
		String username = "0x70unideb";
		String password = "EWGI1853";

		return new String[] { wsdlLocation, username, password };
	}

	private static final CentralControl getCentralControl(String[] args) throws MalformedURLException {
		String wsdlLocation = args[0];
		String username = args[1];
		char[] password = args[2].toCharArray();

		setDefaultAuthentication(username, password);

		URL wsdlURL = new URL(wsdlLocation);
		return new CentralControlServiceService(wsdlURL).getCentralControlPort();

	}

	private static final void setDefaultAuthentication(String username, char[] password) {
		Authenticator.setDefault(new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
	}

}
