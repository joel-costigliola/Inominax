package zorglux.inominax.client;

import static java.lang.Math.random;
import static java.lang.Math.round;
import static zorglux.inominax.exception.FunctionnalException.throwFunctionnalExceptionIfFalse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import zorglux.inominax.shared.TokenSet;

public class NameGenerator {

   private int minNumberOfTokensInName = 2;
   private int maxNumberOfTokensInName = 5;
   private String generatedNameStart = "";
   private String generatedNameEnd = "";
   private String mandatoryStringInGeneratedName = "";
   private List<String> tokens;

   public NameGenerator(int minNumberOfTokensInName, int maxNumberOfTokensInName, List<String> tokens) {
      super();
      checkMinMaxCoherence(minNumberOfTokensInName, maxNumberOfTokensInName);
      setMinNumberOfTokensInName(minNumberOfTokensInName);
      this.maxNumberOfTokensInName = maxNumberOfTokensInName;
      this.tokens = tokens;
   }

   private void checkMinMaxCoherence(int min, int max) {
      throwFunctionnalExceptionIfFalse(min < max, "min number of tokens (" + min + ") > max number of tokens (" + max + ") !");
   }

   public NameGenerator() {}

   public String generateName() {
      String generatedName = "";
      int numberOfRandomTokensInName = randomNumberOfTokens(); // at least = 1
      if (startOfGeneratedNameIsSet()) {
         numberOfRandomTokensInName--;
         generatedName = generatedNameStart;
      }
      if (numberOfRandomTokensInName == 0) { return uppercaseFirstLetter(generatedName); }

      if (endOfGeneratedNameIsSet()) {
         numberOfRandomTokensInName--;
      }
      if (numberOfRandomTokensInName == 0) { return uppercaseFirstLetter(generatedName + generatedNameEnd); }

      // we have at least one token to define.
      int indexOfMandatoryString = 0;
      if (mandatoryStringInGeneratedNameIsSet()) {
         indexOfMandatoryString = randomIntBetween(0, numberOfRandomTokensInName - 1);
      }
      for (int i = 0; i < numberOfRandomTokensInName; i++) {
         // replace a randomtoken by mandatory string at a random index
         if (mandatoryStringInGeneratedNameIsSet() && i == indexOfMandatoryString) {
            generatedName += mandatoryStringInGeneratedName;
         } else {
            generatedName += randomToken();
         }
      }

      // set end of name if requested
      if (endOfGeneratedNameIsSet()) {
         generatedName += generatedNameEnd;
      }

      return uppercaseFirstLetter(generatedName);
   }

   public Set<String> generateNames(int numberOfNames) {
      Set<String> generatedNames = new TreeSet<String>();
      for (int i = 0; i < numberOfNames; i++) {
         generatedNames.add(generateName());
      }
      return generatedNames;
   }

   private String uppercaseFirstLetter(String generatedName) {
      return generatedName.substring(0, 1).toUpperCase() + generatedName.substring(1);
   }

   public void setMaxNumberOfTokensInName(int maxNumberOfTokensInName) {
      if (maxNumberOfTokensInName < minNumberOfTokensInName) {
         this.maxNumberOfTokensInName = minNumberOfTokensInName;
      } else {
         this.maxNumberOfTokensInName = maxNumberOfTokensInName;
      }
   }

   public void setMinNumberOfTokensInName(int minNumberOfTokensInName) {
      if (minNumberOfTokensInName < 1) {
         this.minNumberOfTokensInName = 1;
      } else {
         this.minNumberOfTokensInName = minNumberOfTokensInName;
      }
   }

   public int getMinNumberOfTokensInName() {
      return minNumberOfTokensInName;
   }

   public int getMaxNumberOfTokensInName() {
      return maxNumberOfTokensInName;
   }

   private int randomNumberOfTokens() {
      return randomIntBetween(minNumberOfTokensInName, maxNumberOfTokensInName);
   }

   private int randomIntBetween(int min, int max) {
      return (int) round(random() * (max - min) + min);
   }

   private String randomToken() {
      int randomIndex = (int) round(random() * (tokens.size() - 1));
      return tokens.get(randomIndex);
   }

   public void useTokenSet(TokenSet tokenSet) {
      tokens = new ArrayList<String>(tokenSet.getTokens());
   }

   public void generatedNameMustStartsWith(String startOfName) {
      generatedNameStart = startOfName;
   }

   public void generatedNameMustEndWith(String endOfName) {
      generatedNameEnd = endOfName;
   }

   public void generatedNameMustContain(String stringInName) {
      mandatoryStringInGeneratedName = stringInName;
   }

   public void setTokens(List<String> tokens) {
      this.tokens = tokens;
   }

   private boolean startOfGeneratedNameIsSet() {
      return !isEmptyOrBlank(generatedNameStart);
   }

   private boolean endOfGeneratedNameIsSet() {
      return !isEmptyOrBlank(generatedNameEnd);
   }

   private boolean mandatoryStringInGeneratedNameIsSet() {
      return !isEmptyOrNull(mandatoryStringInGeneratedName);
   }

   private boolean isEmptyOrNull(String token) {
      if (token == null) return true;
      if (token.isEmpty()) return true;
      return false;
   }

   private boolean isEmptyOrBlank(String token) {
      if (token == null) return true;
      if (token.trim().isEmpty()) return true;
      return false;
   }

}
