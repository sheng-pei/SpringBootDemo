package org.example.web.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/request-parameter")
public class RequestParameterController {

    /**
     * Injected by url params, x-www-form-urlencoded body, form-data body and mix of those.
     * Couldn't be injected by json body.
     *
     * @param vo  inject an id parameter into the id property of the argument vo.
     * @param key inject a key parameter into the argument key.
     */
    @PostMapping("/form")
    public Long form(Vo vo, String key, Vo1 vo1) {
        return (long) key.length() + vo.getId() + vo1.getId1();
    }

    /**
     * Add @RequestBody before the argument injected by json body. Otherwise, the argument will
     * be injected by url params, x-www-form-urlencoded body, form-data body and mix of those.
     * Only one @RequestBody can be used in this way.
     *
     * @param vo  support json body. This argument must be packaged.
     * @param key inject a key parameter into the argument key.
     */
    @PostMapping("/json")
    public Long json(@RequestBody Vo vo, String key) {
        return vo.getId() + key.length();
    }

    public static class Vo {
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    public static class Vo1 {
        private Long id1;

        public Long getId1() {
            return id1;
        }

        public void setId1(Long id1) {
            this.id1 = id1;
        }
    }
}
