package com.github.unldenis.dama.core.uti;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Utilita {

  public static String indirizzo_pubblico() throws IOException {
    URL whatismyip = new URL("http://checkip.amazonaws.com");
    BufferedReader in = new BufferedReader(new InputStreamReader(
        whatismyip.openStream()));

    return in.readLine(); //you get the IP as a String
  }

  public static void openWebpage(String urlString) {
    try {
      Desktop.getDesktop().browse(new URL(urlString).toURI());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
