import sys
import re
import json
from sets import Set
from util import sentiment_scores, parse_text, parse_obj_post

"""
parse json file of tweet data
"""
def parse_tweets_file(tweet_file, sent_file):
  term_scores = sentiment_scores(sent_file)
  newterm_scores = {}
  tweets = []
  for line in tweet_file:
    obj = json.JSONDecoder().decode(line)
    if 'created_at' in obj.keys(): # then this is a tweet
      terms = parse_obj_post(obj)
      new_terms = Set()
      # First calculate the sentiment of the tweet
      tweet_score = 0
      for term in terms:
        term_score = term_scores.get(term.lower())
        if term_score: tweet_score += term_score
        else: new_terms.add(term)
      # Then calculate the sentiment of new terms
      if tweet_score != 0:
        for new_term in new_terms:
          newterm_score = newterm_scores.get(new_term)
          if not newterm_score: newterm_score = 0
          newterm_score += tweet_score
          newterm_scores[new_term] = newterm_score
  # Output the result
  result = sorted(newterm_scores.items(), key=lambda x: x[0], reverse=True)
  for term,score in result:
    print term + " " + str(score)


def lines(fp):
  print str(len(fp.readlines()))

def main():
  sent_file = open(sys.argv[1])
  tweet_file = open(sys.argv[2])
  parse_tweets_file(tweet_file, sent_file)

if __name__ == '__main__':
  main()
