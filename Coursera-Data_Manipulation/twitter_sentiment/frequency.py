import sys
import re
import json
import collections
from util import sentiment_scores, parse_text, parse_obj_post


def parse_tweets_file(tweet_file):
  counter = collections.Counter()
  total_term_cnt = 0
  for line in tweet_file:
    obj = json.JSONDecoder().decode(line)
    if 'created_at' in obj.keys(): # then this is a tweet
      terms = parse_obj_post(obj)
      counter.update(terms)
      total_term_cnt += len(terms)
  for w,c in counter.items():
    print "{} {}".format(w, float(c)/total_term_cnt)


def lines(fp):
  print str(len(fp.readlines()))

def main():
  tweet_file = open(sys.argv[1])
  parse_tweets_file(tweet_file)

if __name__ == '__main__':
  main()
