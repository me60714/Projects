AutoSummary

This code generates a summary of a given text by calculating the frequency of words and scoring sentences based on the frequency of words. It uses NLTK to tokenize the text and remove stopwords, and heapq to extract the top sentences based on their scores.

Technical points:

* NLTK library for natural language processing
* Tokenization to split text into words and sentences
* Stopword removal to remove commonly used words
* Word frequency calculation for each word in the text
* Sentence scoring based on the sum of the frequency of words in a sentence
* Heapq library for extracting top sentences based on their scores
