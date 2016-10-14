package net.dkahn.starter.tools.repository.utils;


import com.querydsl.core.types.dsl.BooleanExpression;

/**
 * User: dimitri
 * Date: 10/02/15
 * Time: 15:44
 * Goal:
 */

public class ExpressionHelp {
    private BooleanExpression filterExp = null;

    public static ExpressionHelp instance(){
        return new ExpressionHelp();
    }

    public boolean hasExp(){
        return filterExp != null;
    }

    public BooleanExpression getComputeExp(){
        return filterExp;
    }

    public ExpressionHelp andExp(BooleanExpression exp){
        if(filterExp==null){
            filterExp = exp;
        }else{
            filterExp = filterExp.and(exp);
        }
        return this;
    }


}

