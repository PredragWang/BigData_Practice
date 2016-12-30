Matrix Multiplication
==========
Using Map/Reduce to solve Matrix Multiplication<br>
Two steps:<br>
Step 1.<br>
Mapper:<br>
Input: \<Matrix, x, y, value, L\> <br>
&nbsp;&nbsp;&nbsp;&nbsp;z is the number of times Matrix[x,y] will be used <br>
&nbsp;&nbsp;&nbsp;&nbsp;for the first matrix, L = \<number of columns in output array\> <br>
&nbsp;&nbsp;&nbsp;&nbsp;for the second matrix, L = \<number of rows in the output array\> <br>
Output: <br>
&nbsp;&nbsp;&nbsp;&nbsp;Key = \<i,k,j\>, Value = v <br>
&nbsp;&nbsp;&nbsp;&nbsp;\<i,k\> = the index of the output array, <br>
&nbsp;&nbsp;&nbsp;&nbsp;j = the number to be used in the jth multiplication <br>
&nbsp;&nbsp;&nbsp;&nbsp;there will be exactly two values for each key=\<i,k,j\> <br>
<br>
Reducer:<br>
&nbsp;&nbsp;&nbsp;&nbsp;Input:<br>
&nbsp;&nbsp;&nbsp;&nbsp;Key = \<i,k,j\> (discussed in mapper script)<br>
&nbsp;&nbsp;&nbsp;&nbsp;Value = v <br>
&nbsp;&nbsp;&nbsp;&nbsp;Output:<br>
&nbsp;&nbsp;&nbsp;&nbsp;key = \<x, y\><br>
&nbsp;&nbsp;&nbsp;&nbsp;value = number needs to be summed into output[x, y]<br>
&nbsp;&nbsp;&nbsp;&nbsp;Just multiply the numbers with the same \<i,k,j\><br>
<br>
Step 2.<br>
Mapper: Just pass the output in the first step<br>
Reducer: Merge(or combine) the output in the first step<br>
<br><br>
Execution:<br>
Step 1:<br> 
cmd: hadoop_streaming -input input_mt -output tmp_output -file matrix_mp_mapper.py -mapper matrix_mp_mapper.py -file matrix_mp_tmp_reducer.py -reducer matrix_mp_tmp_reducer.py<br>
Step 2:<br>
cmd: hadoop_streaming -input tmp_output -output output_mt  -mapper /bin/cat -file matrix_mp_reducer.py -reducer matrix_mp_reducer.py<br>
Step 3:<br> 
cmd: hdfs dfs -get output_mt<br>



