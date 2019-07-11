# FPCollector
This Program is collecting False Positives when we using static analysis tool.  
Collecting is based on the version of a target project.  
It means that this tool compare result of PMD   
on current version of the target project with past version of it.  
  
__Using This Tool, You Could Get False Positive Suspects More Easily__

## Package Specification
### 1. clone  
	-Input : Target project git address
	-Output : The path which indicate the location of target project
	-Functions  
		1) Git cloning  

### 2. checkout  
	-Input : target project  
	-Output : The path which indicate the location of target project  
	-Functions  
		1) Git checkout    

### 3. utils
	## 1) Reader
		-Input : A file path which user want to read
		-Output : The file's contents
		-Functions
			1) Read a file and return as its usage
	## 2) ToolExecutor
		-Input : 1) A file path which indicate target project exists
			 2) A rule context
			 3) An information that indicate run tool on Current version or Past version
		-Output : A report file(csv) location
		-Functions
			1) Run a static ananlysis tool
			2) Get the report of analysis

# In Concepting

### 4. collector  
### 5. patternfinder
### 6. ContextExtractor  
### 7. AbstractContext  
### 8. GetCommonContext  
