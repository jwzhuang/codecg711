/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.media.server.impl.dsp.audio.g729;

public class DecAcelp_1 {

	/*-----------------------------------------------------------*
	 *  Function  decod_ACELP()                                  *
	 *  ~~~~~~~~~~~~~~~~~~~~~~~                                  *
	 *   Algebraic codebook decoder.                             *
	 *----------------------------------------------------------*/

	public static void decod_ACELP(
	 int sign,              /* input : signs of 4 pulses     */
	 int index,             /* input : positions of 4 pulses */
	 float cod[]            /* output: innovative codevector */
	)
	{
	   int pos[] = new int[4];
	   int i, j;

	   /* decode the positions of 4 pulses */

	   i = index & 7;
	   pos[0] = i*5;

	   index >>= 3;
	   i = index & 7;
	   pos[1] = i*5 + 1;

	   index >>= 3;
	   i = index & 7;
	   pos[2] = i*5 + 2;

	   index >>= 3;
	   j = index & 1;
	   index >>= 1;
	   i = index & 7;
	   pos[3] = i*5 + 3 + j;

	   /* find the algebraic codeword */

	   for (i = 0; i < LD8KConstants.L_SUBFR; i++) cod[i] = 0;

	   /* decode the signs of 4 pulses */

	   for (j=0; j<4; j++)
	   {

	     i = sign & 1;
	     sign >>= 1;

	     if (i != 0) {
	       cod[pos[j]] = (float)1.0;
	     }
	     else {
	       cod[pos[j]] = (float)-1.0;
	     }
	   }

	   return;
	}

}
