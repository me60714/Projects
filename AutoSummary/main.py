import heapq
import nltk
from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize, sent_tokenize


def get_word_frequency(content):
    """
    This function takes a string as input and returns a dictionary containing the frequency of each word in the input string.
    """
    stop_words = set(stopwords.words("english"))
    words = word_tokenize(content.lower())
    word_frequency = {}
    for word in words:
        if word not in stop_words:
            if word not in word_frequency.keys():
                word_frequency[word] = 1
            else:
                word_frequency[word] += 1
    return word_frequency


def get_sentence_scores(content, word_frequency):
    """
    This function takes a string and a dictionary containing the frequency of each word in the string as input and
    returns a dictionary containing the score of each sentence in the string.
    """
    sentences = sent_tokenize(content)
    sentence_scores = {}
    for sentence in sentences:
        for word in word_tokenize(sentence.lower()):
            if word in word_frequency.keys():
                if len(sentence.split(" ")) < 50:
                    if sentence not in sentence_scores.keys():
                        sentence_scores[sentence] = word_frequency[word]
                    else:
                        sentence_scores[sentence] += word_frequency[word]
    return sentence_scores


def get_summary(content, num_sentences):
    """
    This function takes a string and an integer as input and returns a string containing a summary of the input string.
    """
    word_frequency = get_word_frequency(content)
    sentence_scores = get_sentence_scores(content, word_frequency)
    summary_sentences = heapq.nlargest(num_sentences, sentence_scores, key=sentence_scores.get)
    summary = " ".join(summary_sentences)
    return summary


def main():
    content = input("Please enter the content: \n").strip()
    num_sentences = input("\nPlease enter the number of sentences in the summary: ")
    while not num_sentences.isdigit():
        print("Please enter a valid integer.")
        num_sentences = input("\nPlease enter the number of sentences in the summary: ")
    num_sentences = int(num_sentences)
    summary = get_summary(content, num_sentences)

    print('\nSummary: ')
    print(summary)


if __name__ == "__main__":
    main()
