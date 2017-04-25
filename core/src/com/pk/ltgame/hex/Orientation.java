package com.pk.ltgame.hex;

/**
 * Obiekt orientacji dla Hexów(flat/pointly). Powinny być ustawiane przez odwrócenie x i y, czyli kąty powinny być odwrócone.
 * @author pkale
 */
class Orientation
{
    public Orientation(double matrixEl0, double matrixEl1, double matrixEl2, double matrixEl3, double invMatrixEl0, double invMatrixEl1, double invMatrixEl2, double invMatrixEl3, double beginAngle)
    {
        this.matrixElement0 = matrixEl0;
        this.matrixElement1 = matrixEl1;
        this.matrixElement2 = matrixEl2;
        this.matrixElement3 = matrixEl3;
        this.invertedMatrixElement0 = invMatrixEl0;
        this.invertedMatrixElement1 = invMatrixEl1;
        this.invertedMatrixElement2 = invMatrixEl2;
        this.invertedMatrixElement3 = invMatrixEl3;
        this.beginWithAngle = beginAngle;
    }
    public final double matrixElement0;
    public final double matrixElement1;
    public final double matrixElement2;
    public final double matrixElement3;
    public final double invertedMatrixElement0;
    public final double invertedMatrixElement1;
    public final double invertedMatrixElement2;
    public final double invertedMatrixElement3;
    public final double beginWithAngle;
}
