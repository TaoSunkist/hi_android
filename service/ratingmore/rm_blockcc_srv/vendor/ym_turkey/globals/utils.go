package globals

import "hi_golang/tools/lop"

func FormatArgs2Map(args ...interface{}) map[string]interface{} {
	if len(args) == 0 {
		return nil
	}

	args = args[0].([]interface{})
	argsLen := len(args)
	if argsLen%2 != 0 {
		return nil
	}
	argMap := make(map[string]interface{}, argsLen/2)
	lop.T(args)
	for index, v := range args {
		if index%2 == 0 {
			valueIndex := index + 1
			vStr := v.(string)
			argMap[vStr] = args[valueIndex]
		} else {
			continue
		}
	}
	return argMap
}
