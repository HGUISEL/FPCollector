# FPCollector
## Package Specification
### 1. Init  
	-Input : rule, target project  
	-Output : result of pmd on target project(current version) as a csv file  
	-Functions  
		- Git cloning  
		- Run pmd on target project(current version)  

### 2. Checkout  
	-Input : rule, target project  
	-Output : result of pmd on target project(past version) as a csv file  
	-Functions  
		1) Git checkout  
		2) Run pmd on target project(past version)  

### 3. Collector  
	-Input : Two csv files(results of the project)  
	-Output : A csv file which contains information about FP suspects  
	-Functions  
		1) Compare two result files  
		2) Collect same alarms  
		3) Save it as a csv file  
  
### 4. ContextExtractor  
### 5. AbstractContext  
### 6. GetCommonContext  
