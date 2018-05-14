package movie.client;

import ser321.movie.MovieLibraryGui;
import java.net.*;
import java.io.*;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONArray;
import javax.swing.*;
import java.io.*;
import javax.sound.sampled.*;
import java.beans.*;
import java.net.*;
import java.net.URL;
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.text.html.*;
import javax.swing.filechooser.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.lang.Runtime;
import movie.server.MovieDescription;
import movie.server.MovieDescriptionCollection;
import movie.server.MovieDescriptionHttpProxy;

/**
 * Copyright (c) 2016 James Stokke,
 * Software Engineering,
 * Arizona State University at the Polytechnic campus
 * <p/>
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation version 2
 * of the License.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but without any warranty or fitness for a particular purpose.
 * <p/>
 * Please review the GNU General Public License at:
 * http://www.gnu.org/licenses/gpl-2.0.html
 * see also: https://www.gnu.org/licenses/gpl-faq.html
 * so you are aware of the terms and your rights with regard to this software.
 * Or, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301,USA
 * <p/>
 * Purpose: This class provides a simple controller demonstrating the use
 * of the MovieLibraryGui class. It is intended to be used in creating student
 * solutions to ser321 movie playback and browser assignments.
 * This problem provides for browsing and managing information about
 * video files. It uses a Swing JTree, and JTextField controls to 
 * realize a GUI with a split pane. The left pane contains an expandable
 * JTree of the movie library.
 * This file provides the controler for the client.
 *
 * This software is meant to run on Debian Wheezy Linux
 * <p/>
 * Ser321 Principles of Distributed Software Systems
 * see http://pooh.poly.asu.edu/Ser321
 * @author James Stokke (jstokke@asu.edu) CIDSE - Software Engineering,
 *                       IAFSE, ASU at the Polytechnic campus
 * @date    September, 2016
 **/
public class MovieClient extends MovieLibraryGui implements
                                                       TreeWillExpandListener,
     					               ActionListener,
					               TreeSelectionListener {

   private static final boolean debugOn = true;
   private static final boolean bootstrapOn = false;
   private boolean stopPlaying;         //shared with playing thread.
   public String authorName;


   public MovieClient(String author) {
      super(author);
      System.out.println("This is where the author is created: "+author);
      authorName = author;
      stopPlaying = false;
      /*plotJTA.setText("You selected: ");*/

      actorsJCB.addActionListener(this);
      actorsJCB.setActionCommand("NewActor");
      actorsClearButton.setText("Add");
      actorsClearButton.addActionListener(this);
      actorsClearButton.setActionCommand("AddActor");

      genreJCB.addActionListener(this);
      genreJCB.setActionCommand("NewGenre");
      genreClearButton.setText("Add");
      genreClearButton.addActionListener(this);
      genreClearButton.setActionCommand("AddGenre");

      for(int i=0; i<userMenuItems.length; i++){
         for(int j=0; j<userMenuItems[i].length; j++){
            userMenuItems[i][j].addActionListener(this);
         }
      }
      //tree.addTreeWillExpandListener(this);
      try{
         tree.addTreeSelectionListener(this);
         rebuildTree();
      }catch (Exception ex){
         JOptionPane.showMessageDialog(this,"Handling "+
                                " constructor exception: " + ex.getMessage());
      }
      setVisible(true);
   }

   private void debug(String message) {
      if (debugOn)
         System.out.println("debug: "+message);
   }

   /**
    * create and initialize nodes in the JTree of the left pane.
    * buildInitialTree is called by MovieLibraryGui to initialize the JTree.
    * Classes that extend MovieLibraryGui should override this method to 
    * perform initialization actions specific to the extended class.
    * The default functionality is to set base as the label of root.
    * In your solution, you will probably want to initialize by deserializing
    * your library and displaying the categories and subcategories in the
    * tree.
    * @param root Is the root node of the tree to be initialized.
    * @param base Is the string that is the root node of the tree.
    */
   public void buildInitialTree(DefaultMutableTreeNode root, String base){
      //set up the context and base name
      try{
         root.setUserObject(base);
      }catch (Exception ex){
         JOptionPane.showMessageDialog(this,"exception initial tree:"+ex);
         ex.printStackTrace();
      }
   }

   public void rebuildTree(){
    try {
      String url = "http://"+authorName+":2222/";
      System.out.println("Use This Address Possibly: "+Inet4Address.getLocalHost().getHostAddress());
      System.out.println("Author? "+authorName);
      System.out.println("1.  Opening connection to: "+url);
      MovieDescriptionCollection mc = (MovieDescriptionCollection)new MovieDescriptionHttpProxy(new URL(url));
      MovieDescription newMovie = new MovieDescription("Add New Movie Here", "", "", "", "", "", new String []{}, new String []{});
      mc.add(newMovie);
      String[] result = mc.getTitles();
      tree.removeTreeSelectionListener(this);
      DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
      DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
      clearTree(root, model);
      DefaultMutableTreeNode videoNode = new DefaultMutableTreeNode("Movie");
      model.insertNodeInto(videoNode, root, model.getChildCount(root));
      // put nodes in the tree for all video
      for (int i = 0; i<result.length; i++){
         String aTitle = result[i];
         DefaultMutableTreeNode toAdd = new DefaultMutableTreeNode(aTitle);
         model.insertNodeInto(toAdd, videoNode, model.getChildCount(videoNode));
      }
      // expand all the nodes in the JTree
      for(int r =0; r < tree.getRowCount(); r++){
         tree.expandRow(r);
      }
      tree.addTreeSelectionListener(this);
     
    }catch (Exception e) {
      e.printStackTrace();
      System.out.println("Effort Failed");
    }
   }

   private void clearTree(DefaultMutableTreeNode root, DefaultTreeModel model){
      try{
         DefaultMutableTreeNode next = null;
         int subs = model.getChildCount(root);
         for(int k=subs-1; k>=0; k--){
            next = (DefaultMutableTreeNode)model.getChild(root,k);
            debug("removing node labelled:"+(String)next.getUserObject());
            model.removeNodeFromParent(next);
         }
      }catch (Exception ex) {
         System.out.println("Exception while trying to clear tree:");
         ex.printStackTrace();
      }
   }

   private DefaultMutableTreeNode getSubLabelled(DefaultMutableTreeNode root,
                                                 String label){
      DefaultMutableTreeNode ret = null;
      DefaultMutableTreeNode next = null;
      boolean found = false;
      for(Enumeration e = root.children(); e.hasMoreElements();){
         next = (DefaultMutableTreeNode)e.nextElement();
         debug("sub with label: "+(String)next.getUserObject());
         if (((String)next.getUserObject()).equals(label)){
            debug("found sub with label: "+label);
            found = true;
            break;
         }
      }
      if(found)
         ret = next;
      else
         ret = null;
      return ret;
   }

   public void treeWillCollapse(TreeExpansionEvent tee) {
      debug("In treeWillCollapse with path: "+tee.getPath());
      tree.setSelectionPath(tee.getPath());
   }

   public void treeWillExpand(TreeExpansionEvent tee) {
      debug("In treeWillExpand with path: "+tee.getPath());
      //DefaultMutableTreeNode dmtn =
      //    (DefaultMutableTreeNode)tee.getPath().getLastPathComponent();
      //System.out.println("will expand node: "+dmtn.getUserObject()+
      //		   " whose path is: "+tee.getPath());
   }

   public void valueChanged(TreeSelectionEvent e) {
      try{
         DefaultMutableTreeNode node = (DefaultMutableTreeNode)
            tree.getLastSelectedPathComponent();
         if(node!=null){
            String nodeLabel = (String)node.getUserObject();
            String url = "http://"+authorName+":2222/";
	    System.out.println("Opening connection to: "+url);
	    MovieDescriptionCollection mc = (MovieDescriptionCollection)new MovieDescriptionHttpProxy(new URL(url));tree.removeTreeSelectionListener(this);
	    MovieDescription result = mc.get(nodeLabel);
            debug("In valueChanged. Selected node labelled: "+nodeLabel);
            // is this a terminal node?
            if(node.getChildCount()==0 &&
               (node != (DefaultMutableTreeNode)tree.getModel().getRoot())){
               plotJTA.setText(result.plot);
               titleJTF.setText(nodeLabel);
               ratedJTF.setText(result.rated);
               releasedJTF.setText(result.released);
               fileNameJTF.setText(result.filename);
               runtimeJTF.setText(result.runtime);
               actorsJCB.removeAllItems();
               genreJCB.removeAllItems();
               Iterator it = result.actors.iterator();
               while (it.hasNext())
		  actorsJCB.addItem(it.next().toString());
	       Iterator itr = result.genre.iterator();
               while (itr.hasNext())
		  genreJCB.addItem(itr.next().toString());
               if (!contains(genreJCB, nodeLabel)){
                  genreJCB.removeActionListener(this);
                  /*genreJCB.addItem(nodeLabel);*/
                  int i = 0;
                  while(i<genreJCB.getItemCount()&&
                        !((String)genreJCB.getItemAt(i)).equals(nodeLabel)){
                     i++;
                  }
                  genreJCB.setSelectedIndex(0);
                  genreJCB.addActionListener(this);
               }
            }else{
               plotJTA.setText(result.plot);
            }
         }
      }catch (Exception ex){
         ex.printStackTrace();
      }
      tree.addTreeSelectionListener(this);
   }

   // a method to determine whether a string is already in the combo box
   private boolean contains(JComboBox jcb, String text){
      boolean ret = false;
      for (int i=0; i< jcb.getItemCount(); i++){
         if (((String)jcb.getItemAt(i)).equals(text)){
            ret = true;
         }
      }
      return ret;
   }
   
   public void actionPerformed(ActionEvent e) {
    try{
      tree.removeTreeSelectionListener(this);
      if(e.getActionCommand().equals("Exit")) {
         System.exit(0);
      }else if(e.getActionCommand().equals("NewActor")) {
         debug("new actor selected "+(String)actorsJCB.getSelectedItem());
         /*if(!contains(actorsJCB,(String)actorsJCB.getSelectedItem())){
            actorsJCB.addItem((String)actorsJCB.getSelectedItem());
         }
         /*releasedJTF.setText((String)actorsJCB.getSelectedItem());*/
      }else if(e.getActionCommand().equals("ClearActors")) {
         actorsJCB.removeActionListener(this);
         actorsJCB.removeAllItems();
         actorsJCB.addActionListener(this);
      }else if(e.getActionCommand().equals("ClearGenre")) {
         genreJCB.removeActionListener(this);
         genreJCB.removeAllItems();
         genreJCB.addActionListener(this);
      }else if(e.getActionCommand().equals("AddActor")) {
         actorsJCB.removeActionListener(this);
         actorsJCB.addItem(actorsJCB.getSelectedItem().toString());
         actorsJCB.addActionListener(this);
      }else if(e.getActionCommand().equals("AddGenre")) {
         genreJCB.removeActionListener(this);
         genreJCB.addItem(genreJCB.getSelectedItem().toString());
         genreJCB.addActionListener(this);
      }else if(e.getActionCommand().equals("Save")) {
         plotJTA.append("Save, ");
         rebuildTree();
         // what to do with boolean returns by server methods
         debug("Save "+((true)?"successful":"unsuccessful"));
      }else if(e.getActionCommand().equals("Restore")) {
         plotJTA.append("Restore, ");
      }else if(e.getActionCommand().equals("Tree Refresh")) {
         plotJTA.append("Tree Refresh, ");
         rebuildTree(); // contact the server to obtain all current titles then rebuild
      }else if(e.getActionCommand().equals("Add")) {
	 String url = "http://"+authorName+":2222/";
         MovieDescriptionCollection mc = (MovieDescriptionCollection)new MovieDescriptionHttpProxy(new URL(url));
         String newTitle = titleJTF.getText();
         String newRated = ratedJTF.getText();
         String newPlot = plotJTA.getText();
         String newReleased = releasedJTF.getText();
         String newFileName = fileNameJTF.getText();
         String newRunTime = runtimeJTF.getText();
         String[] newActors = new String[actorsJCB.getItemCount()];
         for (int i=0; i < actorsJCB.getItemCount(); i++)
	    newActors[i] = actorsJCB.getItemAt(i);
	 String[] newGenre = new String[genreJCB.getItemCount()];
         for (int j=0; j < genreJCB.getItemCount(); j++)
	    newGenre[j] = genreJCB.getItemAt(j);
	 MovieDescription aMov = new MovieDescription(newTitle, newRated, newReleased, newRunTime, newPlot, newFileName, newGenre, newActors);
         mc.add(aMov);      
         rebuildTree();
      }else if(e.getActionCommand().equals("Remove")) {
	 String movName = titleJTF.getText();
	 String url = "http://"+authorName+":2222/";
         MovieDescriptionCollection mc = (MovieDescriptionCollection)new MovieDescriptionHttpProxy(new URL(url));
         mc.remove(movName);
         rebuildTree();
      }else if(e.getActionCommand().equals("Play")){
         try{
	    String tempTitle = titleJTF.getText();
	    String tempFileName = fileNameJTF.getText();
	    String url = "http://"+authorName+":2222/";
	    MovieDescriptionCollection mc = (MovieDescriptionCollection)new MovieDescriptionHttpProxy(new URL(url));
	    String aURIPath = downLoadFile(tempFileName);
	    /*new File(aURIPath).toURI().toURL();
	    File forURI = new File(aURIPath);
	    System.out.println("URI Path "+aURIPath);
	    URI uri = new URI();
	    uri = forURI.toURI();*/
            String nodeLabel = tempTitle;
            playMovie(new File(aURIPath).toURI().toURL().toString(), nodeLabel);
         }catch(Exception ex){
            System.out.println("Execption trying to play movie:");
            ex.printStackTrace();
         }
      }
      tree.addTreeSelectionListener(this);
    }catch (Exception f) {
      f.printStackTrace();
      System.out.println("Effort Failed");
    }
   }

   public boolean sezToStop(){
      return stopPlaying;
   }
   
   public String downLoadFile(String filename){
      String playFile = "";
      try {
	Socket sock = new Socket(authorName, 3030);
	BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
	System.out.println("FileName: "+filename);
	OutputStream os = sock.getOutputStream();
	String filePath = "src/java/client/DataClient/"+filename;
	playFile += filePath;
	FileOutputStream fos = new FileOutputStream(filePath);
	BufferedOutputStream bos = new BufferedOutputStream(fos);
	InputStream is = sock.getInputStream();
	int numBytesReceived;
	int bufLen = 1024;
	int count;
	byte bytesToSend[] = filename.getBytes();
	os.write(bytesToSend,0,bytesToSend.length);
	os.flush();
	byte[] buffer = new byte[1024];
	while ((count = is.read(buffer)) >= 0)
	{
	  fos.write(buffer, 0, count);
	}      
	fos.close();
	os.close();
	is.close();
	sock.close();
      }catch (Exception e){
	e.printStackTrace();
      }
      return playFile;
   }


   public static void main(String args[]) {
      try{
         String authorName = "James Stokke's Library";
         if(args.length >=1){
            authorName = args[0];
         }
         System.out.println("calling constructor name "+authorName);
         MovieClient mla = new MovieClient(authorName);
      }catch (Exception ex){
         System.out.println("Exception in main: "+ex.getMessage());
         ex.printStackTrace();
      }
   }
}
