package demo.timeapp.controller.rest;

/**
 * Created by dhval on 4/25/16.
 */


public interface Rest {

/**
    static ResponseEntity<Map<String, String>> msg(Integer code, String describe) {
        return  new ResponseEntity<>(Collections.singletonMap(Integer.toString(code), describe) , HttpStatus.OK);
    }


    default void validate(Object obj, Validator validator, BindingResult result) throws ValidationException {
        validator.validate(obj ,result);
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
    }
     **/
}
