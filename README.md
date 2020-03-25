# sainsburystest
Demo project for Sainsburys job application

TODO: Add tests using JUnit
  This would consist of downloading all the pages from the test site, 
  converting the links to use relative paths.  Then the parsing code in the 
  IndexPage class would need to be abstracted in order to link found URLs to 
  files and load the data from the test files instead of HTTP connections.
  Likely solution, replace direct creation of ProductPage with a Factory 
  class.  The Factory would be passed in to IndexPage, then a Mock Factory
  could be created that did the file reads based on url text.
