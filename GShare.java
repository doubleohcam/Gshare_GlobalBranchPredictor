// Cameron Garretson
// EEL4768
// Prof. Suboh
// GShare Branch Predictor
// Project 2

import java.util.*;
import java.math.*;
import java.io.*;
import java.text.DecimalFormat;

// This was created with the help of the
// skeleton located in Webcourses 
public class GShare
{
  private static DecimalFormat df2 = new DecimalFormat("#.##");

  public static void main(String [] args) throws Exception
  {
    int M = Integer.parseInt(args[0]);
    int N = Integer.parseInt(args[1]);
    int success = 0;
    int failure = 0;
    int GHB = 0;
    boolean OUTCOME = false;

    int[] table = new int[(int) Math.pow(2, (double) M)];

    for (int i = 0; i < Math.pow(2, (double) M); i++)
    {
      table[i] = 2;
    }

    File file = new File(args[2]);
    Scanner input = new Scanner(file);
    int[] xor = new int[N];

    while (input.hasNextLine())
    {
      String hexAddress = input.nextLine();
      String[] truthVal = hexAddress.split(" ");

      // Capture the truth value
      char c = truthVal[1].charAt(0);

      if (c == 't')
        OUTCOME = true;

      else
        OUTCOME= false;

      // Take the hexAddress, convert to bit string
      // this wasn't needed, but I left it as is
      BigInteger big = new BigInteger(truthVal[0], 16);
      String bitString = big.toString(2);

      // convert that string to decimal
      int PC = big.intValue();
      PC = PC / 4;

      double m_index = (double) PC % Math.pow(2, (double) M);
      int n_ext = GHB << (M - N);
      int index = (int) m_index ^ n_ext;
      int PREDICTION = table[index];

      boolean predict = false;

      // Check prediction
      if (PREDICTION >= 2)
        predict = true;

      else
        predict = false;

      // Update counter
      if (OUTCOME)
        PREDICTION++;

      else
        PREDICTION--;

      if (PREDICTION < 0)
        PREDICTION = 0;

      if (PREDICTION > 3)
        PREDICTION = 3;

      table[index] = PREDICTION;

      // update ratio
      if (predict == OUTCOME)
        success++;

      else
        failure++;

      // update GHB
      if (OUTCOME)
      {
        GHB = GHB >> 1;
        GHB = GHB + (int) Math.pow(2, (double) (N - 1));
      }

      else
        GHB = GHB >> 1;
    }

    double denom = (double) success + (double) failure;
    double ratio = (double) failure / denom;
    double newRatio = ratio * 100;
    System.out.println(M + " " + N + " " + df2.format(newRatio) + "%");
  }
}
