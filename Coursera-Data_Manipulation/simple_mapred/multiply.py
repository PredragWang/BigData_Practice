import MapReduce
import sys

"""
Matrix Multiplication Example in the Simple Python MapReduce Framework
"""

mr = MapReduce.MapReduce()

# =============================
# Do not modify above this line
size = 5

def mapper(record):
    m_id = record[0]
    i,j,v = record[1:]
    if m_id == 'a':
        #[i, all, j] => j
        for k in range(size):
            mr.emit_intermediate("{} {}".format(i,k), [j,v])
    else:
        for k in range(size):
            mr.emit_intermediate("{} {}".format(k,j), [i,v])


def reducer(key, list_of_values):
    # key: row and col 
    # value: list of [index, number]
    row,col = map(int, key.split())
    list_of_values.sort()
    n = len(list_of_values)
    ret = 0
    i = 0
    while i < n:
        if i < n-1 and list_of_values[i][0] == list_of_values[i+1][0]:
            ret += list_of_values[i][1]*list_of_values[i+1][1]
            i += 2
        else:
            i += 1
    mr.emit((row, col, ret))

# Do not modify below this line
# =============================
if __name__ == '__main__':
  inputdata = open(sys.argv[1])
  mr.execute(inputdata, mapper, reducer)
