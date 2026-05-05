package com.example.core.saga;

import static com.example.core.enums.EEventSource.*;
import static com.example.core.enums.EEventSource.ORCHESTRATOR;
import static com.example.core.enums.EStatus.FAIL;
import static com.example.core.enums.EStatus.ROLLBACK_PENDING;
import static com.example.core.enums.EStatus.SUCCESS;
import static com.example.core.enums.ETopic.*;

public final class Handler {
    private Handler(){}

    public static final Object[][] HANDLER = {
            {ORCHESTRATOR, SUCCESS, PRODUCT_VALIDATION_SUCCESS},
            {ORCHESTRATOR, FAIL, FINISH_FAIL},

            {PRODUCT_VALIDATION, ROLLBACK_PENDING, PRODUCT_VALIDATION_FAIL},
            {PRODUCT_VALIDATION, FAIL, FINISH_FAIL},
            {PRODUCT_VALIDATION, SUCCESS, INVENTORY_SUCCESS},

            {INVENTORY_SERVICE, ROLLBACK_PENDING, INVENTORY_FAIL},
            {INVENTORY_SERVICE, FAIL, PRODUCT_VALIDATION_FAIL},
            {INVENTORY_SERVICE, SUCCESS, PAYMENT_SUCCESS},

            {PAYMENT_SERVICE, ROLLBACK_PENDING, PAYMENT_FAIL},
            {PAYMENT_SERVICE, FAIL, INVENTORY_FAIL},
            {PAYMENT_SERVICE, SUCCESS, FINISH_SUCCESS}
    };


    public static final Object[][] HANDLER_PRODUCT = {
            {ORCHESTRATOR, SUCCESS, INVENTORY_SUCCESS},
            {ORCHESTRATOR, FAIL, FINISH_FAIL},
            {INVENTORY_SERVICE, ROLLBACK_PENDING, INVENTORY_FAIL},
            {INVENTORY_SERVICE, FAIL, FINISH_FAIL},
            {INVENTORY_SERVICE, SUCCESS, FINISH_SUCCESS}
    };

    public static final int EVENT_SOURCE_INDEX = 0;
    public static final int STATUS_INDEX = 1;
    public static final int TOPIC_INDEX = 2;

}
