# Correcting and detecting codes

This application implements different codes allowing the detection and correction of errors in a binary message, it
provides several features.

## Implemented codes:

- Parity bit
- Cyclic redundancy check
- Internet checksum
- Hamming

## Functionalities:

- **encode:** encodes a message for a given code and message
- **decode:** decodes a message for a given code and message
- **generateMessage:** generates a random message
- **corruptMessage:** corrupts a given message
- **generateErrorDetectingRateGraph:** generates a graph showing the evolution of the detection/correction rate of a
  code according to the evolution of the error rate on a channel

## Usage

This application is a command-line program, it uses the apache common-cli library.

The options are not the same for each functionality, you first must specify the functionality you want to use, then you
need to specify the parameters for this functionality.

If you forget one parameter, the format helper will print the usage of the functionality.

You can always use the -H (help) option to view all the parameters for a given functionality.

Here is the usage of each functionality:

1. Encode:

```
usage: cd-codes encode -C <arg> [-GP <arg>] [-H] -M <arg>
 -C,--code <arg>                   The code to use to encode and decode
                                   messages. Valid codes are:
                                   - parityBit
                                   - CRC
                                   - internetChecksum
                                   - hamming
 -GP,--generatorPolynomial <arg>   The generator polynomial to use to
                                   encode messages (only if CRC code is
                                   chosen, default 1011)
 -H,--help                         Display the usage for the given
                                   functionality
 -M,--message <arg>                The message to be encoded/decoded
   ```

2. Decode:

```
usage: cd-codes decode -C <arg> [-GP <arg>] [-H] -M <arg>
 -C,--code <arg>                   The code to use to encode and decode
                                   messages. Valid codes are:
                                   - parityBit
                                   - CRC
                                   - internetChecksum
                                   - hamming
 -GP,--generatorPolynomial <arg>   The generator polynomial to use to
                                   encode messages (only if CRC code is
                                   chosen, default 1011)
 -H,--help                         Display the usage for the given
                                   functionality
 -M,--message <arg>                The message to be encoded/decoded
   ```

3. Generating a message:

```
usage: cd-codes generateMessage [-H] [-MBS <arg>]
 -H,--help                     Display the usage for the given
                               functionality
 -MBS,--messageBitSize <arg>   Specify the length of a message to be coded
                               (default 8 for parity bit code and CRC, 16
                               for internet checksum and 4 for hamming)
   ```

4. Corrupting a message:

```
usage: cd-codes corruptMessage [-BL <arg>] [-E <arg>] [-H] -M <arg> [-P
       <arg>]
 -BL,--burstLength <arg>   Specify the length of the burst (only if
                           burstError option was specified, default 3)
 -E,--errorModel <arg>     Specify the error model of the transmission
                           channel. Default model is constantError. Valid
                           models are:
                           - constantError
                           - burstError
 -H,--help                 Display the usage for the given functionality
 -M,--message <arg>        The message to be encoded/decoded
 -P <arg>                  The probability of a bit to be corrupted
                           (default 0.1)
   ```

5. Generating the detection/error rate graph:

```
usage: cd-codes generateErrorDetectingRateGraph [-BL <arg>] -C <arg> [-E
       <arg>] [-GP <arg>] [-H] [-I <arg>] [-MaxBY <arg>] [-MaxP <arg>]
       [-MBS <arg>] [-MinBY <arg>] [-MinP <arg>] [-S <arg>]
 -BL,--burstLength <arg>           Specify the length of the burst (only
                                   if burstError option was specified,
                                   default 3)
 -C,--code <arg>                   The code to use to encode and decode
                                   messages. Valid codes are:
                                   - parityBit
                                   - CRC
                                   - internetChecksum
                                   - hamming
 -E,--errorModel <arg>             Specify the error model of the
                                   transmission channel. Default model is
                                   constantError. Valid models are:
                                   - constantError
                                   - burstError
 -GP,--generatorPolynomial <arg>   The generator polynomial to use to
                                   encode messages (only if CRC code is
                                   chosen, default 1011)
 -H,--help                         Display the usage for the given
                                   functionality
 -I <arg>                          The number of iterations per
                                   probability (default 10000)
 -MaxBY <arg>                      The maximum bound of the y axis
                                   (default is 1.0d)
 -MaxP <arg>                       The maximum probability of a bit to be
                                   corrupted (default 0.5)
 -MBS,--messageBitSize <arg>       Specify the length of a message to be
                                   coded (default 8)
 -MinBY <arg>                      The minimum bound of the y axis
                                   (default is 0.0d)
 -MinP <arg>                       The minimum probability of a bit to be
                                   corrupted (default 0.01)
 -S <arg>                          The number of step between the minimum
                                   and the maximum probability (default
                                   50)
   ```

## Example of usage

1. Encoding a message

   Input: `cd-codes encode -C hamming -M 1000`

   Output: `1001011`

2. Decoding a message

   Input: `cd-codes decode -C hamming -M 1001001`

   Output: `1000`

3. Generating a message

   Input: `cd-codes generateMessage -MBS 10`

   Output: `1100111001`

4. Corrupting a message

   Input: `cd-codes corruptMessage -E burstError -M 00000000000000000000`

   Output: `00001010000000010100`

5. Generating a graph showing the evolution of the detection/correction rate of a code according to the evolution of the
   error rate on a channel, it also displays 3 arrays:
   - First array contains the list of probabilities for which the detection/correction rates have
     been generated. Each entry in this table corresponds to a specific probability value within the given range.
     This allows users to identify the specific probabilities at which the rates are calculated.
   - Second array lists the error detection rates corresponding to each probability value. The detection rate indicates
     the percentage of errors that could be successfully identified by the implemented code. Users can analyze how the
     detection rate varies with different probabilities.
   - The third array is only displayed if the specified code can correct error, it contains the error correction rates
     for each probability value. This table provides the percentage of errors that could be successfully corrected by
     the code.
     It gives insights into how the correction rate changes with different probabilities.

   Input: `cd-codes generateErrorDetectingRateGraph -C hamming -MBS 11`

   Output:

   `[0.01, 0.02, 0.03, ...]`

   `[1.0, 0.9992398327632079, 0.9989273263609547, 0.9955604883462819, ...]`

   `[0.9289855072463769, 0.8703914861269479, 0.8082595870206489, ...]`

   ![Alt text](/GenerateHammingGraph.png?raw=true "Evolution of detecting and correcting rate for a given error rate using Hamming code")