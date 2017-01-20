import sys
import re
import json
import collections
from util import hashtags


def parse_tweets_file(tweet_file):
  counter = collections.Counter()
  for line in tweet_file:
    obj = json.JSONDecoder().decode(line)
    if 'created_at' in obj: # then this is a tweet
      htags = hashtags(obj)
      if not htags: continue
      counter.update(htags)
  sorted_ht_count = sorted(counter.items(), key=lambda x:x[1], reverse=True)
  remain = 10
  for w,c in counter.items():
    print "{} {}".format(w, c)
    remain -= 1
    if remain == 0: break

def lines(fp):
  print str(len(fp.readlines()))

def main():
  tweet_file = open(sys.argv[1])
  parse_tweets_file(tweet_file)

if __name__ == '__main__':
  main()
