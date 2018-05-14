#include <FL/Fl.H>
#include <FL/Fl_Window.H>
#include <FL/Fl_Button.H>
#include <FL/Fl_Output.H>
#include <FL/Fl_Tree.H>
#include <FL/Fl_Tree_Item.H>
#include <FL/Fl_Menu_Bar.H>
#include <FL/Fl_Choice.H>
#include <FL/Fl_Text_Display.H>
#include <FL/Fl_Text_Buffer.H>
#include <FL/Fl_Multiline_Input.H>
#include <stdio.h>
#include <iostream>
#include <stdlib.h>

using namespace std;

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
 * Purpose: C++ FLTK view class for Movie client. MovieClientGui is the view
 * for media app. This class is extended by the client controller which is
 * the MovieClient class. MovieClient defines the call-backs for UI controls.
 * It contains sample control functions that respond to button clicks and tree
 * selects.
 * This software is meant to run on Debian Jesse Linux
 * <p/>
 * Ser321 Principles of Distributed Software Systems
 * see http://pooh.poly.asu.edu/Ser321
 * @author James Stokke (jstokke@asu.edu) CIDSE - Software Engineering,
 *                       IAFSE, ASU at the Polytechnic campus
 * @file    MovieClientGui.cpp
 * @date    Steptember, 2016
 **/
class MovieClientGui : public Fl_Window {
protected:

   /**
    * tree is the Fl_Tree object that occupies the left side of the window.
    * this tree control provides the ability to add and remove items and to
    * manipulate and query the tree when an exception occurs.
    */
   Fl_Tree * tree;

   /**
    * titleInput is the Fl_Input object labelled Title
    * Its for the user to enter the media tile (prefix filename).
    */
   Fl_Input * titleInput;

   /**
    * ratedInput is the Fl_Input object labelled Rated
    * Its for the user to enter the movie rating for videos
    */
   Fl_Input * ratedInput;

   /**
    * authorInput is the Fl_Input object labelled Artist
    * Its for the user to enter the artist for music, or for entering the
    * primary actor for videos.
    */
   Fl_Input * runtimeInput;

   /**
    * releasedInput is the Fl_Input object labelled Released
    * Its for the user to enter the released date for videos.
    */
   Fl_Input * releasedInput;

   /**
    * releasedInput is the Fl_Input object labelled Released
    * Its for the user to enter the released date for videos.
    */
   Fl_Input * filenameInput;

   /**
    * plotMLIn is the Fl_Multiline_Input object labelled Plot
    * Its for the user to enter the video's story-line.
    */
   Fl_Multiline_Input * plotMLIn;

   /**
    * menubar is the Fl_Menu_Bar object.
    * Its the container for menus.
    */
   Fl_Menu_Bar *menubar;

   /**
    * Genre is the Fl_Choice object labelled Genre.
    * It provides the ability to enter and select genre
    */
   Fl_Choice *genreChoice;

   /**
    * Actors is the Fl_Choice object labelled Actors.
    * It provides the ability to enter and select actors.
    */
   Fl_Choice *actorsChoice;

public:
   MovieClientGui(const char * name = "Ser321") : Fl_Window(635,350,name) {
      begin();

      menubar = new Fl_Menu_Bar(0, 0, this->w(), 25);
      menubar->add("File/Save");
      menubar->add("File/Restore");
      menubar->add("File/Tree Refresh");
      menubar->add("File/Exit");
      menubar->add("Movie/Remove");
      menubar->add("Movie/Play");

      // create a tree control at position x=10, y=10. Its 150 pixels wide
      // and window height less 20 pixels high. Add some sample tree nodes.
      tree = new Fl_Tree(10, 35, 225, this->h()-45);
      tree->root_label(name);

      /*
       * add a text input control at x=285, y=35 of width 200 pixels and
       * height of 25 pixels. Initialize it contents to media title.
       */
      //Fl_Label * titleLab = new Fl_Label(175, 35, 75, 25);
      //titleLab->value("Title");
      titleInput = new Fl_Input(285, 35, 195, 25);
      titleInput->label("Title");
      titleInput->value("");

      ratedInput = new Fl_Input(530, 35, 75, 25);
      ratedInput->label("Rated");
      ratedInput->value("");

      /*
       * add a text input control at x=250, y=35 of width 200 pixels and
       * height of 25 pixels. Initialize its contents to title.
       */
      runtimeInput = new Fl_Input(315, 75, 85, 25);
      runtimeInput->label("Runtime");
      runtimeInput->value("");

      releasedInput = new Fl_Input(480, 75, 120, 25);
      releasedInput->label("Released");
      releasedInput->value("");

      plotMLIn = new Fl_Multiline_Input(285, 115, 325, 145);
      plotMLIn->label("Plot");
      plotMLIn->value("");

      filenameInput = new Fl_Input(315, 275, 230, 25);
      filenameInput->label("Filename");
      filenameInput->value("");
 
      // create the media type drop-down (input_choice)
      actorsChoice = new Fl_Choice(295, 315, 150, 25, "Actors");
      /*actorsChoice->add("Will Smith");
      actorsChoice->add("Anne Hathaway");
      */
      actorsChoice->value(0); // set the control initially to Will Smith

      // create the media type drop-down (input_choice)
      genreChoice = new Fl_Choice(502, 315, 120, 25, "Genre");
      /*
      genreChoice->add("Action");
      genreChoice->add("Animation");
      */
      genreChoice->value(0); // set the control initially to Action

      end();
      show();
   }
};
