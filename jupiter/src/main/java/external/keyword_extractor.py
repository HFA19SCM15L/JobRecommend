import sys
import spacy
import nltk

nltk.download('punkt')
from nltk.corpus import stopwords
from rake_nltk import Rake

# Ensure you have the NLTK stopwords dataset downloaded
nltk.download('stopwords')

# Load spaCy model
nlp = spacy.load("en_core_web_sm")


def extract_keywords(text):
    # Process the text with spaCy
    doc = nlp(text)

    # Use Rake algorithm
    rake = Rake(stopwords=stopwords.words('english'), min_length=1, max_length=1)

    # Extract keywords
    rake.extract_keywords_from_text(text)

    # Get ranked phrases with scores
    ranked_phrases = rake.get_ranked_phrases()

    # Return only the top 3 results
    top_three_phrases = ranked_phrases[:3]

    return top_three_phrases


# Example usage
if __name__ == "__main__":
    if len(sys.argv) > 1:  # Checking if any additional arguments were provided
        input_text = sys.argv[1]
    else:
        input_text = """
        Experienced web developer needed with expertise in HTML, CSS, JavaScript, and Python. 
        Familiarity with React and Node.js is a plus. Responsibilities include developing 
        front-end interfaces and efficient backend services.
        """
    print(extract_keywords(input_text))
