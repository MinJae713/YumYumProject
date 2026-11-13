package com.ssafy.yumyum.common.exception;

import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.ssafy.yumyum.common.controller.ApiResponse;
import com.ssafy.yumyum.common.exception.commonExceptions.DeleteFailedException;
import com.ssafy.yumyum.common.exception.commonExceptions.InsertFailedException;
import com.ssafy.yumyum.common.exception.commonExceptions.UpdateFailedException;
import com.ssafy.yumyum.domain.badge.exception.NoBadgeCategoryException;
import com.ssafy.yumyum.domain.badge.exception.NoBadgeException;
import com.ssafy.yumyum.domain.badge.exception.NoBadgeListException;
import com.ssafy.yumyum.domain.badge.exception.NoBadgeProgressException;
import com.ssafy.yumyum.domain.badge.exception.NoUserBadgeListException;
import com.ssafy.yumyum.domain.exercise.exception.ExcerciseDateInputException;
import com.ssafy.yumyum.domain.exercise.exception.ExerciseRecordDeleteException;
import com.ssafy.yumyum.domain.exercise.exception.ExerciseRecordInsertException;
import com.ssafy.yumyum.domain.exercise.exception.ExerciseRecordUpdateException;
import com.ssafy.yumyum.domain.exercise.exception.NoExerciseCatalogException;
import com.ssafy.yumyum.domain.exercise.exception.NoExerciseCategoryException;
import com.ssafy.yumyum.domain.exercise.exception.NoExerciseRecordException;
import com.ssafy.yumyum.domain.group.exception.GroupInviteAlreadyExistsException;
import com.ssafy.yumyum.domain.group.exception.GroupInviteAlreadyResponseException;
import com.ssafy.yumyum.domain.group.exception.GroupInviteNoFoundException;
import com.ssafy.yumyum.domain.group.exception.GroupMemberAlreadyExistsException;
import com.ssafy.yumyum.domain.group.exception.GroupMemberNotFoundException;
import com.ssafy.yumyum.domain.group.exception.GroupMemberScoreUpdateException;
import com.ssafy.yumyum.domain.group.exception.GroupNotFoundException;
import com.ssafy.yumyum.domain.group.exception.GroupPermissionDeniedException;
import com.ssafy.yumyum.domain.meal.exception.ExistMealException;
import com.ssafy.yumyum.domain.meal.exception.ExistMealItemException;
import com.ssafy.yumyum.domain.meal.exception.FoodCacheNotFoundException;
import com.ssafy.yumyum.domain.meal.exception.FoodMatchFailedException;
import com.ssafy.yumyum.domain.meal.exception.FoodNotFoundException;
import com.ssafy.yumyum.domain.meal.exception.MealDateInputException;
import com.ssafy.yumyum.domain.meal.exception.MealInsertException;
import com.ssafy.yumyum.domain.meal.exception.NoFoodException;
import com.ssafy.yumyum.domain.meal.exception.NoFoodUnitException;
import com.ssafy.yumyum.domain.meal.exception.NoGroupMealException;
import com.ssafy.yumyum.domain.meal.exception.NoMealAIDetailException;
import com.ssafy.yumyum.domain.meal.exception.NoMealCreatorTypeException;
import com.ssafy.yumyum.domain.meal.exception.NoMealDataException;
import com.ssafy.yumyum.domain.meal.exception.NoMealItemDataException;
import com.ssafy.yumyum.domain.meal.exception.NoMealTypeException;
import com.ssafy.yumyum.domain.meal.exception.NoUpdateFoodTypeException;
import com.ssafy.yumyum.domain.research.exception.ResearchLogNotFoundException;
import com.ssafy.yumyum.domain.user.exception.InvalidPasswordException;
import com.ssafy.yumyum.domain.user.exception.InvalidUserRoleException;
import com.ssafy.yumyum.domain.user.exception.UserAlreadyExistException;
import com.ssafy.yumyum.domain.user.exception.UserHeightException;
import com.ssafy.yumyum.domain.user.exception.UserNotFoundException;
import com.ssafy.yumyum.domain.user.exception.UserWeightException;
import com.ssafy.yumyum.domain.user.exception.WeightLogNotFoundException;
import com.ssafy.yumyum.security.exception.RefreshTokenInvalidException;
import com.ssafy.yumyum.security.exception.RefreshTokenMissingException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ApiResponse<Object>> bindException(BindException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.of(
                        HttpStatus.BAD_REQUEST,
                        e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                        Collections.emptyMap()
                ));

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ApiResponse<Object>> illegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.of(
                        HttpStatus.BAD_REQUEST,
                        e.getMessage(),
                        Collections.emptyMap()
                ));

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiResponse<Object>> handleHandlerMethodValidationException(HandlerMethodValidationException e) {
        String message = e.getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.of(
                        HttpStatus.BAD_REQUEST,
                        message,
                        Collections.emptyMap()
                ));

    }
    /********************************************************************************/
    /*                        Common CustomException                                    */
    /********************************************************************************/
    @ExceptionHandler({
            InsertFailedException.class,
            UpdateFailedException.class,
    })
    public ResponseEntity<ApiResponse<Object>> handleCommonBadRequestException(RuntimeException e){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.of(HttpStatus.BAD_REQUEST, e.getMessage(), null));
    }
    @ExceptionHandler(MismatchedInputException.class)
    public ResponseEntity<ApiResponse<Object>> handleTypeMismatchException(MismatchedInputException e) {
        String errorMessage = "입력 값과 타입이 일치하지 않습니다.";
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.of(HttpStatus.BAD_REQUEST, errorMessage, null));
    }

    @ExceptionHandler({
            DeleteFailedException.class
    })
    public ResponseEntity<ApiResponse<Object>> handleCommonInternalServerErrorException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
    }

    /********************************************************************************/
    /*                        AI CustomException                                    */
    /********************************************************************************/

    /********************************************************************************/
    /*                        Badge CustomException                                 */
    /********************************************************************************/
    
    @ExceptionHandler({
    	NoBadgeListException.class,
    	NoUserBadgeListException.class,
    	NoBadgeCategoryException.class,
    	NoBadgeException.class,
    	NoBadgeProgressException.class
    })
    public ResponseEntity<ApiResponse<Object>> handleBadgeInternalServerException(RuntimeException e) {
    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    			.body(ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
    }

    /********************************************************************************/
    /*                        Dashboard CustomException                             */
    /********************************************************************************/

    /********************************************************************************/
    /*                        Exercise CustomException                              */
    /********************************************************************************/
    @ExceptionHandler({
    	NoExerciseCatalogException.class,
    	NoExerciseCategoryException.class,
    	NoExerciseRecordException.class,
    	ExcerciseDateInputException.class
    })
    public ResponseEntity<ApiResponse<Object>> handleExerciseBadRequestException(RuntimeException e) {
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    			.body(ApiResponse.of(HttpStatus.BAD_REQUEST, e.getMessage(), null));
    }
    
    @ExceptionHandler({
    	ExerciseRecordDeleteException.class,
    	ExerciseRecordInsertException.class,
    	ExerciseRecordUpdateException.class,
    })
    public ResponseEntity<ApiResponse<Object>> handleExerciseInternalServerException(RuntimeException e) {
    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    			.body(ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
    }

    /********************************************************************************/
    /*                        Group CustomException                                 */
    /********************************************************************************/

    @ExceptionHandler({
    	GroupMemberScoreUpdateException.class
    })
    public ResponseEntity<ApiResponse<Object>> handleGroupInternalServerException(RuntimeException e){
        return ResponseEntity
        		.status(HttpStatus.INTERNAL_SERVER_ERROR)
        		.body(ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
    }

    @ExceptionHandler({
            GroupPermissionDeniedException.class
    })
    public ResponseEntity<ApiResponse<Object>> handleGroupForbiddenException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.of(HttpStatus.FORBIDDEN, e.getMessage()));
    }

    @ExceptionHandler({
            GroupMemberNotFoundException.class,
            GroupNotFoundException.class,
            GroupInviteNoFoundException.class
    })
    public ResponseEntity<ApiResponse<Object>> handleGroupNotFoundException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.of(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler({
            GroupMemberAlreadyExistsException.class,
            GroupInviteAlreadyExistsException.class,
            GroupInviteAlreadyResponseException.class
    })
    public ResponseEntity<ApiResponse<Object>> handleGroupConflictException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.of(HttpStatus.CONFLICT, e.getMessage()));
    }
    
    /********************************************************************************/
    /*                        Mainpage CustomException                              */
    /********************************************************************************/

    /********************************************************************************/
    /*                        Meal CustomException                                  */
    /********************************************************************************/
    @ExceptionHandler({
    	NoMealDataException.class,
    	NoFoodException.class,
    	NoFoodUnitException.class,
    	NoGroupMealException.class,
    	ExistMealException.class,
    	ExistMealItemException.class,
    	MealDateInputException.class
	})
    public ResponseEntity<ApiResponse<Object>> handleMealBadRequestException(RuntimeException e){
        return ResponseEntity
        		.status(HttpStatus.BAD_REQUEST)
        		.body(ApiResponse.of(HttpStatus.BAD_REQUEST, e.getMessage(), null));
    }

    @ExceptionHandler({
    	NoMealTypeException.class,
    	NoMealCreatorTypeException.class,
    	NoMealItemDataException.class,
    	NoUpdateFoodTypeException.class,
    	MealInsertException.class,
        FoodMatchFailedException.class,
    	NoMealAIDetailException.class
    })
    public ResponseEntity<ApiResponse<Object>> handleMealInternalServerException(RuntimeException e){
        return ResponseEntity
        		.status(HttpStatus.INTERNAL_SERVER_ERROR)
        		.body(ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
    }

    @ExceptionHandler({
            FoodCacheNotFoundException.class,
            FoodNotFoundException.class
    })
    public ResponseEntity<ApiResponse<Object>> handleMealNotFoundException(RuntimeException e){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.of(HttpStatus.NOT_FOUND, e.getMessage(), null));

    }

    /********************************************************************************/
    /*                        Notification CustomException                          */
    /********************************************************************************/

    /********************************************************************************/
    /*                        ResearchLog CustomException                              */
    /********************************************************************************/
    @ExceptionHandler({
        ResearchLogNotFoundException.class
    })
    public ResponseEntity<ApiResponse<Object>> handleResearchLogNotFoundException(RuntimeException e){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.of(HttpStatus.NOT_FOUND, e.getMessage(), null));

    }

    /********************************************************************************/
    /*                        User CustomException                                  */
    /********************************************************************************/
    @ExceptionHandler({
        InvalidUserRoleException.class
    })
    public ResponseEntity<ApiResponse<Object>> handleUserBadRequestException(RuntimeException e){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.of(HttpStatus.BAD_REQUEST, e.getMessage(), null));

    }

    @ExceptionHandler({
            UserAlreadyExistException.class
    })
    public ResponseEntity<ApiResponse<Object>> handleUserConflictException(RuntimeException e){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.of(HttpStatus.CONFLICT, e.getMessage(), null));

    }

    @ExceptionHandler({
            InvalidPasswordException.class
    })
    public ResponseEntity<ApiResponse<Object>> handleUserUnauthorizedException(RuntimeException e){
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED).body(ApiResponse.of(HttpStatus.UNAUTHORIZED, e.getMessage(), null));
    }

    @ExceptionHandler({
            UserNotFoundException.class,
            UserHeightException.class,
            UserWeightException.class,
            WeightLogNotFoundException.class
    })
    public ResponseEntity<ApiResponse<Object>> handleUserNotFoundException(RuntimeException e){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND).body(ApiResponse.of(HttpStatus.NOT_FOUND, e.getMessage(), null));
    }

    /********************************************************************************/
    /*                        Security CustomException                              */
    /********************************************************************************/

    @ExceptionHandler({
        RefreshTokenMissingException.class,
        RefreshTokenInvalidException.class
    })
    public ResponseEntity<ApiResponse<Object>> handleSecurityUnauthorizedException(RuntimeException e){
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED).body(ApiResponse.of(HttpStatus.UNAUTHORIZED, e.getMessage(), null));
    }

}
