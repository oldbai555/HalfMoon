package com.oldbai.halfmoon.exception;

import com.oldbai.halfmoon.response.ResponseResult;
import com.oldbai.halfmoon.response.ResponseState;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 自定义异常页面跳转页面
 *
 * @author 老白
 */
@RestController
public class UtilController {
    @GetMapping("/403")
    public ResponseResult page403() {
        ResponseResult failed = new ResponseResult(ResponseState.ERROE_403);
        return failed;
    }

    @GetMapping("/404")
    public ResponseResult page404() {
        ResponseResult failed = new ResponseResult(ResponseState.ERROE_404);
        return failed;
    }

    @GetMapping("/504")
    public ResponseResult page504() {
        ResponseResult failed = new ResponseResult(ResponseState.ERROE_504);
        return failed;
    }

    @GetMapping("/505")
    public ResponseResult page505() {
        ResponseResult failed = new ResponseResult(ResponseState.ERROE_505);
        return failed;
    }
}
