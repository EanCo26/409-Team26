package team26.cw;

class MethodDetails {
    private String methodName = "";
    private int methodDecisions = 0;
    private int beginLine = 0;
    private int endLine = 0;

    public void setMethodName(String mName){
        this.methodName = mName;
    }
    public void setMethodLines(int beginLine, int endLine){
        this.beginLine = beginLine;
        this.endLine = endLine;
    }
    public String getMethodName(){
        return this.methodName;
    }
    public int getBeginLine(){
        return this.beginLine;
    }
    public int getEndLine(){
        return this.endLine;
    }

    public void addMethodDecisions(int mDecisions){
        this.methodDecisions += mDecisions;
    }
    public int getMethodDecisions(){
        return this.methodDecisions;
    }
}
