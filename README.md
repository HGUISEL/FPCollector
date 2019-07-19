# FPCollector
This Program is collecting False Positives when we using static analysis tool.  
Collecting is based on the version of a target project.  
It means that this tool compare result of PMD   
on current version of the target project with past version of it.  
  
__Using This Tool, You Could Get False Positive Suspects More Easily__

## Specification
### 1. fpcollector
	* Input : Four Text Files which are  
			 a. Target projects' git address 
			 b. Tool's rule context 
			 c. Output path
			 d. Tool's path which could make run tool
	* Output : One CSV File which is Result of FP Collecting
	* Functions
		1)Main
			a. Instanciate FPCollector
			b. Start to run
		2)FPCollector
			a. Read the Text Files
			b. clone Target Project
			c. execute Tool to cloned Target Project
			d. collect useful information from tool report
			e. checkout Target project to a year ago
			f. execute Tool to being checkout Target Project
			g. collect useful information from tool report
			h. collect line informations
			i. compare current project's line information
			j. get suspects from comparison
			
### 2. clone  
	-Input : Target project git address
	-Output : The path which indicate the location of target project
	-Functions  
		1) Git cloning  

### 3. checkout  
	-Input : target project path 
	-Output : The path which indicate the location of target project  
	-Functions  
		1) Git checkout    

### 4. reportanalysis
	1) getDirLine
		-Parameter : ArrayList<String[]> report
		-Return : ArraytList<SimpleEntry<String, String>> info
			 (ArrayList contains a Pair which contains pair of String.
			 Key is Directory Path, Value is line number)
	2) gitBlame : To get line information
		-Parameter : ArrayList<SimpleEntry<String, String>> reportInfo, String path
			    (Key is Directory path, Value is Line number.
			    String path is for setting working directory to execute git blame)
		-Return : HashMap<String, SimpleEntry<String, String>> blameInfo
			  (Using HashMap to deal with duplicated data easily.
			  Key of HashMap is Code Information of the Line.
			  Value of HashMap is a Pair which contains Dir and Line num)
	3) suspectsFinder : This method compare current line information and past line and return duplicated ones.
		-Parameter : HashMap<String, SimpleEntry<String, String>> current,
			     HashMap<String, SimpleEntry<String, String>> past
		-Return : HashMap<String, SimpleEntry<String, String>>
			  
### 5. utils
	1) Reader
		-Input : A file path which user want to read
		-Output : The file's contents
		-Functions
			1) readTextFile
				-Parameter : String path(path to read file)
				-Return : String str(file contents)
			2) readPMDReport
				-Parameter : String path(path where pmd report is located)
				-Return : ArrayList<String[]> report
					 (after split each report line to deal with only dir and line easily.
					 report[0] : directory and line
					 report[1] : violation message)
			
	2) ToolExecutor
		-Input : 1) A file path which indicate target project exists
			 2) A rule context
			 3) An information that indicate run tool on Current version or Past version
		-Output : A report file(csv) location
		-Functions
			1) Run a static ananlysis tool
			2) Get the report of analysis
	3) Wirter
		-Input : suspects' information, output path
		-Output : a csv file which include suspects' directory path and line number

# In Concepting


### * Get Line Context of suspects 

