package zorglux.inominax.exception;

import java.io.Serializable;

public class FunctionnalException extends RuntimeException implements Serializable{

   private static final long serialVersionUID = -2489340994691939901L;

   public FunctionnalException() {
   }

   public FunctionnalException(String message, Throwable cause) {
      super(message, cause);
   }

   public FunctionnalException(String message) {
      super(message);
   }

   public FunctionnalException(Throwable cause) {
      super(cause);
   }

   public static void throwFunctionnalExceptionIfNull(Object objectToTestForNull, String descriptionOfObject) {
      if (objectToTestForNull == null) {
         throw new FunctionnalException("Expected a non null" + descriptionOfObject);
      }
   }

   public static void throwFunctionnalExceptionIfTrue(boolean expression, String message) {
      if (expression) {
         throw new FunctionnalException(message);
      }
   }

   public static void throwFunctionnalExceptionIfFalse(boolean expression, String message) {
      throwFunctionnalExceptionIfTrue(!expression, message);
   }

}
