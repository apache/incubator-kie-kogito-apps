import * as Primitive from '../xml-primitives';

// Source files:
// http://dmg.org/pmml/v4-4/pmml-4-4.xsd

interface BaseType {
  _exists: boolean;
  _namespace: string;
}
interface _AbsoluteExponentialKernelType extends BaseType {
  description?: string;
  gamma?: number;
  noiseVariance?: number;
  Extension?: ExtensionType[];
  Lambda?: LambdaType[];
}
interface AbsoluteExponentialKernelType extends _AbsoluteExponentialKernelType {
  constructor: { new (): AbsoluteExponentialKernelType };
}

export type ACTIVATIONFUNCTION =
  | 'threshold'
  | 'logistic'
  | 'tanh'
  | 'identity'
  | 'exponential'
  | 'reciprocal'
  | 'square'
  | 'Gauss'
  | 'sine'
  | 'cosine'
  | 'Elliott'
  | 'arctan'
  | 'rectifier'
  | 'radialBasis';
interface _ACTIVATIONFUNCTION extends Primitive._string {
  content: ACTIVATIONFUNCTION;
}

interface _AggregateType extends BaseType {
  field: string;
  function: AggregateTypeFunctionType;
  groupField: string;
  sqlWhere: string;
  Extension?: ExtensionType[];
}
interface AggregateType extends _AggregateType {
  constructor: { new (): AggregateType };
}

type AggregateTypeFunctionType =
  | 'count'
  | 'sum'
  | 'average'
  | 'min'
  | 'max'
  | 'multiset';
interface _AggregateTypeFunctionType extends Primitive._string {
  content: AggregateTypeFunctionType;
}

export type ALGORITHMTYPE = 'iforest' | 'ocsvm' | 'clusterMeanDist' | 'other';
interface _ALGORITHMTYPE extends Primitive._string {
  content: ALGORITHMTYPE;
}

interface _AlternateType extends BaseType {
  AnyDistribution: AnyDistributionType;
  GaussianDistribution: GaussianDistributionType;
  PoissonDistribution: PoissonDistributionType;
  UniformDistribution: UniformDistributionType;
}
interface AlternateType extends _AlternateType {
  constructor: { new (): AlternateType };
}

interface _AnnotationType extends BaseType {
  Extension?: ExtensionType[];
}
interface AnnotationType extends _AnnotationType {
  constructor: { new (): AnnotationType };
}

interface _AnomalyDetectionModelType extends BaseType {
  algorithmName: string;
  algorithmType: ALGORITHMTYPE;
  functionName: MININGFUNCTION;
  isScorable: boolean;
  modelName: string;
  sampleDataSize: string;
  AnomalyDetectionModel: AnomalyDetectionModelType;
  AssociationModel: AssociationModelType;
  BaselineModel: BaselineModelType;
  BayesianNetworkModel: BayesianNetworkModelType;
  ClusteringModel: ClusteringModelType;
  Extension?: ExtensionType[];
  GaussianProcessModel: GaussianProcessModelType;
  GeneralRegressionModel: GeneralRegressionModelType;
  LocalTransformations?: LocalTransformationsType;
  MeanClusterDistances?: MeanClusterDistancesType;
  MiningModel: MiningModelType;
  MiningSchema: MiningSchemaType;
  ModelVerification?: ModelVerificationType;
  NaiveBayesModel: NaiveBayesModelType;
  NearestNeighborModel: NearestNeighborModelType;
  NeuralNetwork: NeuralNetworkType;
  Output?: OutputType;
  RegressionModel: RegressionModelType;
  RuleSetModel: RuleSetModelType;
  Scorecard: ScorecardType;
  SequenceModel: SequenceModelType;
  SupportVectorMachineModel: SupportVectorMachineModelType;
  TextModel: TextModelType;
  TimeSeriesModel: TimeSeriesModelType;
  TreeModel: TreeModelType;
}
interface AnomalyDetectionModelType extends _AnomalyDetectionModelType {
  constructor: { new (): AnomalyDetectionModelType };
}

interface _AnovaRowType extends BaseType {
  degreesOfFreedom: number;
  fValue: number;
  meanOfSquares: number;
  pValue: number;
  sumOfSquares: number;
  type: AnovaRowTypeTypeType;
  Extension?: ExtensionType[];
}
interface AnovaRowType extends _AnovaRowType {
  constructor: { new (): AnovaRowType };
}

type AnovaRowTypeTypeType = 'Model' | 'Error' | 'Total';
interface _AnovaRowTypeTypeType extends Primitive._string {
  content: AnovaRowTypeTypeType;
}

interface _AnovaType extends BaseType {
  target: string;
  AnovaRow: AnovaRowType[];
  Extension?: ExtensionType[];
}
interface AnovaType extends _AnovaType {
  constructor: { new (): AnovaType };
}

interface _AntecedentSequenceType extends BaseType {
  Extension?: ExtensionType[];
  SequenceReference: SequenceReferenceType;
  Time?: TimeType;
}
interface AntecedentSequenceType extends _AntecedentSequenceType {
  constructor: { new (): AntecedentSequenceType };
}

interface _AnyDistributionType extends BaseType {
  mean: number;
  variance: number;
  Extension?: ExtensionType[];
}
interface AnyDistributionType extends _AnyDistributionType {
  constructor: { new (): AnyDistributionType };
}

interface _ApplicationType extends BaseType {
  name: string;
  version: string;
  Extension?: ExtensionType[];
}
interface ApplicationType extends _ApplicationType {
  constructor: { new (): ApplicationType };
}

interface _ApplyType extends BaseType {
  defaultValue: string;
  function: string;
  invalidValueTreatment: INVALIDVALUETREATMENTMETHOD;
  mapMissingTo: string;
  Aggregate?: AggregateType[];
  Apply?: ApplyType[];
  Constant?: ConstantType[];
  Discretize?: DiscretizeType[];
  Extension?: ExtensionType[];
  FieldRef?: FieldRefType[];
  Lag?: LagType[];
  MapValues?: MapValuesType[];
  NormContinuous?: NormContinuousType[];
  NormDiscrete?: NormDiscreteType[];
  TextIndex?: TextIndexType[];
}
interface ApplyType extends _ApplyType {
  constructor: { new (): ApplyType };
}

interface _ARDSquaredExponentialKernelType extends BaseType {
  description?: string;
  gamma?: number;
  noiseVariance?: number;
  Extension?: ExtensionType[];
  Lambda?: LambdaType[];
}
interface ARDSquaredExponentialKernelType
  extends _ARDSquaredExponentialKernelType {
  constructor: { new (): ARDSquaredExponentialKernelType };
}

interface _ARIMAType extends BaseType {
  constantTerm: number;
  predictionMethod: ARIMATypePredictionMethodType;
  RMSE: number;
  transformation: ARIMATypeTransformationType;
  DynamicRegressor?: DynamicRegressorType[];
  Extension?: ExtensionType[];
  MaximumLikelihoodStat?: MaximumLikelihoodStatType;
  NonseasonalComponent?: NonseasonalComponentType;
  OutlierEffect?: OutlierEffectType[];
  SeasonalComponent?: SeasonalComponentType;
}
interface ARIMAType extends _ARIMAType {
  constructor: { new (): ARIMAType };
}

type ARIMATypePredictionMethodType =
  | 'conditionalLeastSquares'
  | 'exactLeastSquares';
interface _ARIMATypePredictionMethodType extends Primitive._string {
  content: ARIMATypePredictionMethodType;
}

type ARIMATypeTransformationType = 'none' | 'logarithmic' | 'squareroot';
interface _ARIMATypeTransformationType extends Primitive._string {
  content: ARIMATypeTransformationType;
}

interface _ARMAPartType extends BaseType {
  constant: number;
  p: number;
  q: number;
  AR: ARType;
  Extension?: ExtensionType[];
  MA: MAType;
}
interface ARMAPartType extends _ARMAPartType {
  constructor: { new (): ARMAPartType };
}

interface _ArrayType extends BaseType {
  n?: number;
  type: ArrayTypeTypeType;
}
export interface ArrayType extends _ArrayType {
  constructor: { new (): ArrayType };
}
export var ArrayType: { new (): ArrayType };

type ArrayTypeTypeType = 'int' | 'real' | 'string';
interface _ArrayTypeTypeType extends Primitive._string {
  content: ArrayTypeTypeType;
}

interface _ARType extends BaseType {
  Array: ArrayType;
  Extension?: ExtensionType[];
}
interface ARType extends _ARType {
  constructor: { new (): ARType };
}

interface _AssociationModelType extends BaseType {
  algorithmName: string;
  avgNumberOfItemsPerTA: number;
  functionName: MININGFUNCTION;
  isScorable: boolean;
  lengthLimit: number;
  maxNumberOfItemsPerTA: number;
  minimumConfidence: number;
  minimumSupport: number;
  modelName: string;
  numberOfItems: number;
  numberOfItemsets: number;
  numberOfRules: number;
  numberOfTransactions: number;
  AssociationRule?: AssociationRuleType[];
  Extension?: ExtensionType[];
  Item?: ItemType[];
  Itemset?: ItemsetType[];
  LocalTransformations?: LocalTransformationsType;
  MiningSchema: MiningSchemaType;
  ModelStats?: ModelStatsType;
  ModelVerification?: ModelVerificationType;
  Output?: OutputType;
}
interface AssociationModelType extends _AssociationModelType {
  constructor: { new (): AssociationModelType };
}

interface _AssociationRuleType extends BaseType {
  affinity?: number;
  antecedent: string;
  confidence: number;
  consequent: string;
  id?: string;
  leverage?: number;
  lift?: number;
  support: number;
  Extension?: ExtensionType[];
}
interface AssociationRuleType extends _AssociationRuleType {
  constructor: { new (): AssociationRuleType };
}

interface _AttributeType extends BaseType {
  partialScore?: number;
  reasonCode: string;
  ComplexPartialScore?: ComplexPartialScoreType;
  CompoundPredicate: CompoundPredicateType;
  Extension?: ExtensionType[];
  False: FalseType;
  SimplePredicate: SimplePredicateType;
  SimpleSetPredicate: SimpleSetPredicateType;
  True: TrueType;
}
interface AttributeType extends _AttributeType {
  constructor: { new (): AttributeType };
}

interface _BaseCumHazardTablesType extends BaseType {
  maxTime?: number;
  BaselineCell: BaselineCellType[];
  BaselineStratum: BaselineStratumType[];
  Extension?: ExtensionType[];
}
interface BaseCumHazardTablesType extends _BaseCumHazardTablesType {
  constructor: { new (): BaseCumHazardTablesType };
}

interface _BaselineCellType extends BaseType {
  cumHazard: number;
  time: number;
  Extension?: ExtensionType[];
}
interface BaselineCellType extends _BaselineCellType {
  constructor: { new (): BaselineCellType };
}

interface _BaselineModelType extends BaseType {
  algorithmName?: string;
  functionName: MININGFUNCTION;
  isScorable?: boolean;
  modelName?: string;
  Extension?: ExtensionType[];
  LocalTransformations?: LocalTransformationsType;
  MiningSchema: MiningSchemaType;
  ModelExplanation?: ModelExplanationType;
  ModelStats?: ModelStatsType;
  ModelVerification?: ModelVerificationType;
  Output?: OutputType;
  Targets?: TargetsType;
  TestDistributions: TestDistributionsType;
}
interface BaselineModelType extends _BaselineModelType {
  constructor: { new (): BaselineModelType };
}

interface _BaselineStratumType extends BaseType {
  label: string;
  maxTime: number;
  value: string;
  BaselineCell?: BaselineCellType[];
  Extension?: ExtensionType[];
}
interface BaselineStratumType extends _BaselineStratumType {
  constructor: { new (): BaselineStratumType };
}

export type BASELINETESTSTATISTIC =
  | 'zValue'
  | 'chiSquareIndependence'
  | 'chiSquareDistribution'
  | 'CUSUM'
  | 'scalarProduct';
interface _BASELINETESTSTATISTIC extends Primitive._string {
  content: BASELINETESTSTATISTIC;
}

interface _BaselineType extends BaseType {
  AnyDistribution: AnyDistributionType;
  CountTable: COUNTTABLETYPE;
  FieldRef: FieldRefType[];
  GaussianDistribution: GaussianDistributionType;
  NormalizedCountTable: COUNTTABLETYPE;
  PoissonDistribution: PoissonDistributionType;
  UniformDistribution: UniformDistributionType;
}
interface BaselineType extends _BaselineType {
  constructor: { new (): BaselineType };
}

interface _BayesianNetworkModelType extends BaseType {
  algorithmName: string;
  functionName: MININGFUNCTION;
  inferenceMethod: INFERENCETYPE;
  isScorable: boolean;
  modelName: string;
  modelType: BNTYPE;
  BayesianNetworkNodes: BayesianNetworkNodesType;
  Extension?: ExtensionType[];
  LocalTransformations?: LocalTransformationsType;
  MiningSchema: MiningSchemaType;
  ModelExplanation?: ModelExplanationType;
  ModelStats?: ModelStatsType;
  ModelVerification?: ModelVerificationType;
  Output?: OutputType;
  Targets?: TargetsType;
}
interface BayesianNetworkModelType extends _BayesianNetworkModelType {
  constructor: { new (): BayesianNetworkModelType };
}

interface _BayesianNetworkNodesType extends BaseType {
  ContinuousNode: ContinuousNodeType[];
  DiscreteNode: DiscreteNodeType[];
  Extension?: ExtensionType[];
}
interface BayesianNetworkNodesType extends _BayesianNetworkNodesType {
  constructor: { new (): BayesianNetworkNodesType };
}

interface _BayesInputsType extends BaseType {
  BayesInput: BayesInputType[];
  Extension?: ExtensionType[];
}
interface BayesInputsType extends _BayesInputsType {
  constructor: { new (): BayesInputsType };
}

interface _BayesInputType extends BaseType {
  fieldName: string;
  DerivedField?: DerivedFieldType;
  Extension?: ExtensionType[];
  PairCounts: PairCountsType[];
  TargetValueStats: TargetValueStatsType;
}
interface BayesInputType extends _BayesInputType {
  constructor: { new (): BayesInputType };
}

interface _BayesOutputType extends BaseType {
  fieldName: string;
  Extension?: ExtensionType[];
  TargetValueCounts: TargetValueCountsType;
}
interface BayesOutputType extends _BayesOutputType {
  constructor: { new (): BayesOutputType };
}

interface _BinarySimilarityType extends BaseType {
  c00Parameter: number;
  c01Parameter: number;
  c10Parameter: number;
  c11Parameter: number;
  d00Parameter: number;
  d01Parameter: number;
  d10Parameter: number;
  d11Parameter: number;
  Extension?: ExtensionType[];
}
interface BinarySimilarityType extends _BinarySimilarityType {
  constructor: { new (): BinarySimilarityType };
}

interface _BlockIndicatorType extends BaseType {
  field: string;
  Extension?: ExtensionType[];
}
interface BlockIndicatorType extends _BlockIndicatorType {
  constructor: { new (): BlockIndicatorType };
}

export type BNTYPE = 'General' | 'TAN' | 'Markov-blanket';
interface _BNTYPE extends Primitive._string {
  content: BNTYPE;
}

interface _BoundaryValueMeansType extends BaseType {
  Array: ArrayType;
  Extension?: ExtensionType[];
}
interface BoundaryValueMeansType extends _BoundaryValueMeansType {
  constructor: { new (): BoundaryValueMeansType };
}

interface _BoundaryValuesType extends BaseType {
  Array: ArrayType;
  Extension?: ExtensionType[];
}
interface BoundaryValuesType extends _BoundaryValuesType {
  constructor: { new (): BoundaryValuesType };
}

interface _CategoricalPredictorType extends BaseType {
  coefficient: number;
  name: string;
  value: string;
  Extension?: ExtensionType[];
}
interface CategoricalPredictorType extends _CategoricalPredictorType {
  constructor: { new (): CategoricalPredictorType };
}

interface _CategoriesType extends BaseType {
  Category: CategoryType[];
  Extension?: ExtensionType[];
}
interface CategoriesType extends _CategoriesType {
  constructor: { new (): CategoriesType };
}

interface _CategoryType extends BaseType {
  value: string;
  Extension?: ExtensionType[];
}
interface CategoryType extends _CategoryType {
  constructor: { new (): CategoryType };
}

export type CATSCORINGMETHOD = 'majorityVote' | 'weightedMajorityVote';
interface _CATSCORINGMETHOD extends Primitive._string {
  content: CATSCORINGMETHOD;
}

interface _CharacteristicsType extends BaseType {
  Characteristic: CharacteristicType[];
  Extension?: ExtensionType[];
}
interface CharacteristicsType extends _CharacteristicsType {
  constructor: { new (): CharacteristicsType };
}

interface _CharacteristicType extends BaseType {
  baselineScore: number;
  name?: string;
  reasonCode: string;
  Attribute: AttributeType[];
  Extension?: ExtensionType[];
}
interface CharacteristicType extends _CharacteristicType {
  constructor: { new (): CharacteristicType };
}

interface _ChebychevType extends BaseType {
  Extension?: ExtensionType[];
}
interface ChebychevType extends _ChebychevType {
  constructor: { new (): ChebychevType };
}

interface _ChildParentType extends BaseType {
  childField: string;
  isRecursive?: ChildParentTypeIsRecursiveType;
  parentField: string;
  parentLevelField?: string;
  Extension?: ExtensionType[];
  FieldColumnPair?: FieldColumnPairType[];
  InlineTable: InlineTableType;
  TableLocator: TableLocatorType;
}
interface ChildParentType extends _ChildParentType {
  constructor: { new (): ChildParentType };
}

type ChildParentTypeIsRecursiveType = 'no' | 'yes';
interface _ChildParentTypeIsRecursiveType extends Primitive._string {
  content: ChildParentTypeIsRecursiveType;
}

interface _CityBlockType extends BaseType {
  Extension?: ExtensionType[];
}
interface CityBlockType extends _CityBlockType {
  constructor: { new (): CityBlockType };
}

interface _ClassLabelsType extends BaseType {
  Array: ArrayType;
  Extension?: ExtensionType[];
}
interface ClassLabelsType extends _ClassLabelsType {
  constructor: { new (): ClassLabelsType };
}

interface _ClusteringFieldType extends BaseType {
  compareFunction?: COMPAREFUNCTION;
  field: string;
  fieldWeight: number;
  isCenterField: ClusteringFieldTypeIsCenterFieldType;
  similarityScale?: number;
  Comparisons?: ComparisonsType;
  Extension?: ExtensionType[];
}
interface ClusteringFieldType extends _ClusteringFieldType {
  constructor: { new (): ClusteringFieldType };
}

type ClusteringFieldTypeIsCenterFieldType = 'true' | 'false';
interface _ClusteringFieldTypeIsCenterFieldType extends Primitive._string {
  content: ClusteringFieldTypeIsCenterFieldType;
}

interface _ClusteringModelQualityType extends BaseType {
  dataName?: string;
  SSB?: number;
  SSE?: number;
  Extension?: ExtensionType[];
}
interface ClusteringModelQualityType extends _ClusteringModelQualityType {
  constructor: { new (): ClusteringModelQualityType };
}

interface _ClusteringModelType extends BaseType {
  algorithmName?: string;
  functionName: MININGFUNCTION;
  isScorable: boolean;
  modelClass: ClusteringModelTypeModelClassType;
  modelName?: string;
  numberOfClusters: number;
  Cluster: ClusterType[];
  ClusteringField: ClusteringFieldType[];
  ComparisonMeasure: ComparisonMeasureType;
  Extension?: ExtensionType[];
  LocalTransformations?: LocalTransformationsType;
  MiningSchema: MiningSchemaType;
  MissingValueWeights?: MissingValueWeightsType;
  ModelExplanation?: ModelExplanationType;
  ModelStats?: ModelStatsType;
  ModelVerification?: ModelVerificationType;
  Output?: OutputType;
}
interface ClusteringModelType extends _ClusteringModelType {
  constructor: { new (): ClusteringModelType };
}

type ClusteringModelTypeModelClassType = 'centerBased' | 'distributionBased';
interface _ClusteringModelTypeModelClassType extends Primitive._string {
  content: ClusteringModelTypeModelClassType;
}

interface _ClusterType extends BaseType {
  id?: string;
  name?: string;
  size?: number;
  Array?: ArrayType;
  Covariances?: CovariancesType;
  Extension?: ExtensionType[];
  KohonenMap?: KohonenMapType;
  Partition?: PartitionType;
}
interface ClusterType extends _ClusterType {
  constructor: { new (): ClusterType };
}

interface _CoefficientsType extends BaseType {
  absoluteValue?: number;
  numberOfCoefficients?: number;
  Coefficient: CoefficientType[];
  Extension?: ExtensionType[];
}
interface CoefficientsType extends _CoefficientsType {
  constructor: { new (): CoefficientsType };
}

interface _CoefficientType extends BaseType {
  value?: number;
  Extension?: ExtensionType[];
}
interface CoefficientType extends _CoefficientType {
  constructor: { new (): CoefficientType };
}

export type COMPAREFUNCTION =
  | 'absDiff'
  | 'gaussSim'
  | 'delta'
  | 'equal'
  | 'table';
interface _COMPAREFUNCTION extends Primitive._string {
  content: COMPAREFUNCTION;
}

interface _ComparisonMeasureType extends BaseType {
  compareFunction: COMPAREFUNCTION;
  kind: ComparisonMeasureTypeKindType;
  maximum?: number;
  minimum?: number;
  binarySimilarity: BinarySimilarityType;
  chebychev: ChebychevType;
  cityBlock: CityBlockType;
  euclidean: EuclideanType;
  Extension?: ExtensionType[];
  jaccard: JaccardType;
  minkowski: MinkowskiType;
  simpleMatching: SimpleMatchingType;
  squaredEuclidean: SquaredEuclideanType;
  tanimoto: TanimotoType;
}
interface ComparisonMeasureType extends _ComparisonMeasureType {
  constructor: { new (): ComparisonMeasureType };
}

type ComparisonMeasureTypeKindType = 'distance' | 'similarity';
interface _ComparisonMeasureTypeKindType extends Primitive._string {
  content: ComparisonMeasureTypeKindType;
}

interface _ComparisonsType extends BaseType {
  Extension?: ExtensionType[];
  Matrix: MatrixType;
}
interface ComparisonsType extends _ComparisonsType {
  constructor: { new (): ComparisonsType };
}

interface _ComplexPartialScoreType extends BaseType {
  Aggregate: AggregateType;
  Apply: ApplyType;
  Constant: ConstantType;
  Discretize: DiscretizeType;
  Extension?: ExtensionType[];
  FieldRef: FieldRefType;
  Lag: LagType;
  MapValues: MapValuesType;
  NormContinuous: NormContinuousType;
  NormDiscrete: NormDiscreteType;
  TextIndex: TextIndexType;
}
interface ComplexPartialScoreType extends _ComplexPartialScoreType {
  constructor: { new (): ComplexPartialScoreType };
}

interface _CompoundPredicateType extends BaseType {
  booleanOperator: CompoundPredicateTypeBooleanOperatorType;
  CompoundPredicate: CompoundPredicateType[];
  Extension?: ExtensionType[];
  False: FalseType[];
  SimplePredicate: SimplePredicateType[];
  SimpleSetPredicate: SimpleSetPredicateType[];
  True: TrueType[];
}
interface CompoundPredicateType extends _CompoundPredicateType {
  constructor: { new (): CompoundPredicateType };
}

type CompoundPredicateTypeBooleanOperatorType =
  | 'or'
  | 'and'
  | 'xor'
  | 'surrogate';
interface _CompoundPredicateTypeBooleanOperatorType extends Primitive._string {
  content: CompoundPredicateTypeBooleanOperatorType;
}

interface _CompoundRuleType extends BaseType {
  CompoundPredicate: CompoundPredicateType;
  CompoundRule: CompoundRuleType[];
  Extension?: ExtensionType[];
  False: FalseType;
  SimplePredicate: SimplePredicateType;
  SimpleRule: SimpleRuleType[];
  SimpleSetPredicate: SimpleSetPredicateType;
  True: TrueType;
}
interface CompoundRuleType extends _CompoundRuleType {
  constructor: { new (): CompoundRuleType };
}

interface _ConfusionMatrixType extends BaseType {
  ClassLabels: ClassLabelsType;
  Extension?: ExtensionType[];
  Matrix: MatrixType;
}
interface ConfusionMatrixType extends _ConfusionMatrixType {
  constructor: { new (): ConfusionMatrixType };
}

interface _ConsequentSequenceType extends BaseType {
  Extension?: ExtensionType[];
  SequenceReference: SequenceReferenceType;
  Time?: TimeType;
}
interface ConsequentSequenceType extends _ConsequentSequenceType {
  constructor: { new (): ConsequentSequenceType };
}

interface _ConstantType extends Primitive._string {
  dataType: DATATYPE;
  missing: boolean;
}
interface ConstantType extends _ConstantType {
  constructor: { new (): ConstantType };
}

interface _ConstraintsType extends BaseType {
  maximumAntConsSeparationTime: number;
  maximumItemsetSeparationTime: number;
  maximumNumberOfAntecedentItems: number;
  maximumNumberOfConsequentItems: number;
  maximumNumberOfItems: number;
  maximumTotalSequenceTime: number;
  minimumAntConsSeparationTime: number;
  minimumConfidence: number;
  minimumItemsetSeparationTime: number;
  minimumLift: number;
  minimumNumberOfAntecedentItems: number;
  minimumNumberOfConsequentItems: number;
  minimumNumberOfItems: number;
  minimumSupport: number;
  minimumTotalSequenceTime: number;
  Extension?: ExtensionType[];
}
interface ConstraintsType extends _ConstraintsType {
  constructor: { new (): ConstraintsType };
}

interface _ContinuousConditionalProbabilityType extends BaseType {
  count?: number;
  ContinuousDistribution: ContinuousDistributionType[];
  Extension?: ExtensionType[];
  ParentValue?: ParentValueType[];
}
interface ContinuousConditionalProbabilityType
  extends _ContinuousConditionalProbabilityType {
  constructor: { new (): ContinuousConditionalProbabilityType };
}

interface _ContinuousDistributionType extends BaseType {
  Extension?: ExtensionType[];
  LognormalDistributionForBN: LognormalDistributionForBNType;
  NormalDistributionForBN: NormalDistributionForBNType;
  TriangularDistributionForBN: TriangularDistributionForBNType;
  UniformDistributionForBN: UniformDistributionForBNType;
}
interface ContinuousDistributionType extends _ContinuousDistributionType {
  constructor: { new (): ContinuousDistributionType };
}

interface _ContinuousNodeType extends BaseType {
  count?: number;
  name: string;
  ContinuousConditionalProbability: ContinuousConditionalProbabilityType[];
  ContinuousDistribution: ContinuousDistributionType[];
  DerivedField?: DerivedFieldType[];
  Extension?: ExtensionType[];
}
interface ContinuousNodeType extends _ContinuousNodeType {
  constructor: { new (): ContinuousNodeType };
}

export type CONTSCORINGMETHOD = 'median' | 'average' | 'weightedAverage';
interface _CONTSCORINGMETHOD extends Primitive._string {
  content: CONTSCORINGMETHOD;
}

interface _ContStatsType extends BaseType {
  totalSquaresSum: number;
  totalValuesSum: number;
  Array?: ArrayType[];
  Extension?: ExtensionType[];
  Interval?: IntervalType[];
}
interface ContStatsType extends _ContStatsType {
  constructor: { new (): ContStatsType };
}

interface _ConType extends BaseType {
  from: string;
  weight: number;
  Extension?: ExtensionType[];
}
interface ConType extends _ConType {
  constructor: { new (): ConType };
}

interface _CorrelationFieldsType extends BaseType {
  Array: ArrayType;
  Extension?: ExtensionType[];
}
interface CorrelationFieldsType extends _CorrelationFieldsType {
  constructor: { new (): CorrelationFieldsType };
}

interface _CorrelationMethodsType extends BaseType {
  Extension?: ExtensionType[];
  Matrix: MatrixType;
}
interface CorrelationMethodsType extends _CorrelationMethodsType {
  constructor: { new (): CorrelationMethodsType };
}

interface _CorrelationsType extends BaseType {
  CorrelationFields: CorrelationFieldsType;
  CorrelationMethods?: CorrelationMethodsType;
  CorrelationValues: CorrelationValuesType;
  Extension?: ExtensionType[];
}
interface CorrelationsType extends _CorrelationsType {
  constructor: { new (): CorrelationsType };
}

interface _CorrelationValuesType extends BaseType {
  Extension?: ExtensionType[];
  Matrix: MatrixType;
}
interface CorrelationValuesType extends _CorrelationValuesType {
  constructor: { new (): CorrelationValuesType };
}

interface _CountsType extends BaseType {
  cardinality: number;
  invalidFreq: number;
  missingFreq: number;
  totalFreq: number;
  Extension?: ExtensionType[];
}
interface CountsType extends _CountsType {
  constructor: { new (): CountsType };
}

interface _COUNTTABLETYPE extends BaseType {
  sample?: number;
  Extension?: ExtensionType[];
  FieldValue: FieldValueType[];
  FieldValueCount: FieldValueCountType[];
}
export interface COUNTTABLETYPE extends _COUNTTABLETYPE {
  constructor: { new (): COUNTTABLETYPE };
}
export var COUNTTABLETYPE: { new (): COUNTTABLETYPE };

interface _CovariancesType extends BaseType {
  Extension?: ExtensionType[];
  Matrix: MatrixType;
}
interface CovariancesType extends _CovariancesType {
  constructor: { new (): CovariancesType };
}

interface _CovariateListType extends BaseType {
  Extension?: ExtensionType[];
  Predictor?: PredictorType[];
}
interface CovariateListType extends _CovariateListType {
  constructor: { new (): CovariateListType };
}

export type CUMULATIVELINKFUNCTION =
  | 'logit'
  | 'probit'
  | 'cloglog'
  | 'loglog'
  | 'cauchit';
interface _CUMULATIVELINKFUNCTION extends Primitive._string {
  content: CUMULATIVELINKFUNCTION;
}

interface _DataDictionaryType extends BaseType {
  numberOfFields: number;
  DataField: DataFieldType[];
  Extension?: ExtensionType[];
  Taxonomy?: TaxonomyType[];
}
interface DataDictionaryType extends _DataDictionaryType {
  constructor: { new (): DataDictionaryType };
}

interface _DataFieldType extends BaseType {
  dataType: DATATYPE;
  displayName: string;
  isCyclic: DataFieldTypeIsCyclicType;
  name: string;
  optype: OPTYPE;
  taxonomy: string;
  Extension?: ExtensionType[];
  Interval?: IntervalType[];
  Value?: ValueType[];
}
interface DataFieldType extends _DataFieldType {
  constructor: { new (): DataFieldType };
}

type DataFieldTypeIsCyclicType = '0' | '1';
interface _DataFieldTypeIsCyclicType extends Primitive._string {
  content: DataFieldTypeIsCyclicType;
}

export type DATATYPE =
  | 'string'
  | 'integer'
  | 'float'
  | 'double'
  | 'boolean'
  | 'date'
  | 'time'
  | 'dateTime'
  | 'dateDaysSince[0]'
  | 'dateDaysSince[1960]'
  | 'dateDaysSince[1970]'
  | 'dateDaysSince[1980]'
  | 'timeSeconds'
  | 'dateTimeSecondsSince[0]'
  | 'dateTimeSecondsSince[1960]'
  | 'dateTimeSecondsSince[1970]'
  | 'dateTimeSecondsSince[1980]';
interface _DATATYPE extends Primitive._string {
  content: DATATYPE;
}

interface _DecisionsType extends BaseType {
  businessProblem: string;
  description: string;
  Decision: DecisionType[];
  Extension?: ExtensionType[];
}
interface DecisionsType extends _DecisionsType {
  constructor: { new (): DecisionsType };
}

interface _DecisionTreeType extends BaseType {
  algorithmName: string;
  functionName: MININGFUNCTION;
  missingValuePenalty: number;
  missingValueStrategy: MISSINGVALUESTRATEGY;
  modelName: string;
  noTrueChildStrategy: NOTRUECHILDSTRATEGY;
  splitCharacteristic: DecisionTreeTypeSplitCharacteristicType;
  Extension?: ExtensionType[];
  LocalTransformations?: LocalTransformationsType;
  ModelStats?: ModelStatsType;
  Node: NodeType;
  Output?: OutputType;
  ResultField?: ResultFieldType[];
  Targets?: TargetsType;
}
interface DecisionTreeType extends _DecisionTreeType {
  constructor: { new (): DecisionTreeType };
}

type DecisionTreeTypeSplitCharacteristicType = 'binarySplit' | 'multiSplit';
interface _DecisionTreeTypeSplitCharacteristicType extends Primitive._string {
  content: DecisionTreeTypeSplitCharacteristicType;
}

interface _DecisionType extends BaseType {
  description: string;
  displayValue: string;
  value: string;
  Extension?: ExtensionType[];
}
interface DecisionType extends _DecisionType {
  constructor: { new (): DecisionType };
}

interface _DefineFunctionType extends BaseType {
  dataType: DATATYPE;
  name: string;
  optype: OPTYPE;
  Aggregate: AggregateType;
  Apply: ApplyType;
  Constant: ConstantType;
  Discretize: DiscretizeType;
  Extension?: ExtensionType[];
  FieldRef: FieldRefType;
  Lag: LagType;
  MapValues: MapValuesType;
  NormContinuous: NormContinuousType;
  NormDiscrete: NormDiscreteType;
  ParameterField: ParameterFieldType[];
  TextIndex: TextIndexType;
}
interface DefineFunctionType extends _DefineFunctionType {
  constructor: { new (): DefineFunctionType };
}

export type DELIMITER = 'sameTimeWindow' | 'acrossTimeWindows';
interface _DELIMITER extends Primitive._string {
  content: DELIMITER;
}

interface _DelimiterType extends BaseType {
  delimiter: DELIMITER;
  gap: GAP;
  Extension?: ExtensionType[];
}
interface DelimiterType extends _DelimiterType {
  constructor: { new (): DelimiterType };
}

interface _DenominatorType extends BaseType {
  Extension?: ExtensionType[];
  NonseasonalFactor?: NonseasonalFactorType;
  SeasonalFactor?: SeasonalFactorType;
}
interface DenominatorType extends _DenominatorType {
  constructor: { new (): DenominatorType };
}

interface _DerivedFieldType extends BaseType {
  dataType: DATATYPE;
  displayName: string;
  name: string;
  optype: OPTYPE;
  Aggregate: AggregateType;
  Apply: ApplyType;
  Constant: ConstantType;
  Discretize: DiscretizeType;
  Extension?: ExtensionType[];
  FieldRef: FieldRefType;
  Lag: LagType;
  MapValues: MapValuesType;
  NormContinuous: NormContinuousType;
  NormDiscrete: NormDiscreteType;
  TextIndex: TextIndexType;
  Value?: ValueType[];
}
interface DerivedFieldType extends _DerivedFieldType {
  constructor: { new (): DerivedFieldType };
}

interface _DiscreteConditionalProbabilityType extends BaseType {
  count?: number;
  Extension?: ExtensionType[];
  ParentValue: ParentValueType[];
  ValueProbability: ValueProbabilityType[];
}
interface DiscreteConditionalProbabilityType
  extends _DiscreteConditionalProbabilityType {
  constructor: { new (): DiscreteConditionalProbabilityType };
}

interface _DiscreteNodeType extends BaseType {
  count?: number;
  name: string;
  DerivedField?: DerivedFieldType[];
  DiscreteConditionalProbability: DiscreteConditionalProbabilityType[];
  Extension?: ExtensionType[];
  ValueProbability: ValueProbabilityType[];
}
interface DiscreteNodeType extends _DiscreteNodeType {
  constructor: { new (): DiscreteNodeType };
}

interface _DiscretizeBinType extends BaseType {
  binValue: string;
  Extension?: ExtensionType[];
  Interval: IntervalType;
}
interface DiscretizeBinType extends _DiscretizeBinType {
  constructor: { new (): DiscretizeBinType };
}

interface _DiscretizeType extends BaseType {
  dataType: DATATYPE;
  defaultValue: string;
  field: string;
  mapMissingTo: string;
  DiscretizeBin?: DiscretizeBinType[];
  Extension?: ExtensionType[];
}
interface DiscretizeType extends _DiscretizeType {
  constructor: { new (): DiscretizeType };
}

interface _DiscrStatsType extends BaseType {
  modalValue: string;
  Array?: ArrayType[];
  Extension?: ExtensionType[];
}
interface DiscrStatsType extends _DiscrStatsType {
  constructor: { new (): DiscrStatsType };
}

interface _DocumentTermMatrixType extends BaseType {
  Extension?: ExtensionType[];
  Matrix: MatrixType;
}
interface DocumentTermMatrixType extends _DocumentTermMatrixType {
  constructor: { new (): DocumentTermMatrixType };
}

interface _DynamicRegressorType extends BaseType {
  delay: number;
  field: string;
  futureValuesMethod: DynamicRegressorTypeFutureValuesMethodType;
  targetField: string;
  transformation: DynamicRegressorTypeTransformationType;
  Denominator?: DenominatorType;
  Extension?: ExtensionType[];
  Numerator?: NumeratorType;
  RegressorValues?: RegressorValuesType;
}
interface DynamicRegressorType extends _DynamicRegressorType {
  constructor: { new (): DynamicRegressorType };
}

type DynamicRegressorTypeFutureValuesMethodType =
  | 'constant'
  | 'trend'
  | 'stored'
  | 'otherModel'
  | 'userSupplied';
interface _DynamicRegressorTypeFutureValuesMethodType
  extends Primitive._string {
  content: DynamicRegressorTypeFutureValuesMethodType;
}

type DynamicRegressorTypeTransformationType =
  | 'none'
  | 'logarithmic'
  | 'squareroot';
interface _DynamicRegressorTypeTransformationType extends Primitive._string {
  content: DynamicRegressorTypeTransformationType;
}

export type ELEMENTID = string;
type _ELEMENTID = Primitive._string;

interface _EuclideanType extends BaseType {
  Extension?: ExtensionType[];
}
interface EuclideanType extends _EuclideanType {
  constructor: { new (): EuclideanType };
}

interface _EventValuesType extends BaseType {
  Extension?: ExtensionType[];
  Interval?: IntervalType[];
  Value?: ValueType[];
}
interface EventValuesType extends _EventValuesType {
  constructor: { new (): EventValuesType };
}

interface _ExponentialSmoothingType extends BaseType {
  RMSE: number;
  transformation: ExponentialSmoothingTypeTransformationType;
  Level: LevelType;
  Seasonality_ExpoSmooth?: Seasonality_ExpoSmoothType;
  TimeValue?: TimeValueType[];
  Trend_ExpoSmooth?: Trend_ExpoSmoothType;
}
interface ExponentialSmoothingType extends _ExponentialSmoothingType {
  constructor: { new (): ExponentialSmoothingType };
}

type ExponentialSmoothingTypeTransformationType =
  | 'none'
  | 'logarithmic'
  | 'squareroot';
interface _ExponentialSmoothingTypeTransformationType
  extends Primitive._string {
  content: ExponentialSmoothingTypeTransformationType;
}

interface _ExtensionType extends Primitive._any {
  extender?: string;
  name?: string;
  value?: string;
}
interface ExtensionType extends _ExtensionType {
  constructor: { new (): ExtensionType };
}

interface _FactorListType extends BaseType {
  Extension?: ExtensionType[];
  Predictor?: PredictorType[];
}
interface FactorListType extends _FactorListType {
  constructor: { new (): FactorListType };
}

interface _FalseType extends BaseType {
  Extension?: ExtensionType[];
}
interface FalseType extends _FalseType {
  constructor: { new (): FalseType };
}

interface _FieldColumnPairType extends BaseType {
  column: string;
  field: string;
  Extension?: ExtensionType[];
}
interface FieldColumnPairType extends _FieldColumnPairType {
  constructor: { new (): FieldColumnPairType };
}

export type FIELDNAME = string;
type _FIELDNAME = Primitive._string;

interface _FieldRefType extends BaseType {
  field: string;
  mapMissingTo: string;
  Extension?: ExtensionType[];
}
interface FieldRefType extends _FieldRefType {
  constructor: { new (): FieldRefType };
}

export type FIELDUSAGETYPE =
  | 'active'
  | 'predicted'
  | 'target'
  | 'supplementary'
  | 'group'
  | 'order'
  | 'frequencyWeight'
  | 'analysisWeight';
interface _FIELDUSAGETYPE extends Primitive._string {
  content: FIELDUSAGETYPE;
}

interface _FieldValueCountType extends BaseType {
  count: number;
  field: string;
  value: string;
  Extension?: ExtensionType[];
}
interface FieldValueCountType extends _FieldValueCountType {
  constructor: { new (): FieldValueCountType };
}

interface _FieldValueType extends BaseType {
  field: string;
  value: string;
  Extension?: ExtensionType[];
  FieldValue: FieldValueType[];
  FieldValueCount: FieldValueCountType[];
}
interface FieldValueType extends _FieldValueType {
  constructor: { new (): FieldValueType };
}

interface _FinalNoiseType extends BaseType {
  Array: ArrayType;
}
interface FinalNoiseType extends _FinalNoiseType {
  constructor: { new (): FinalNoiseType };
}

interface _FinalNuType extends BaseType {
  Array: ArrayType;
}
interface FinalNuType extends _FinalNuType {
  constructor: { new (): FinalNuType };
}

interface _FinalOmegaType extends BaseType {
  Matrix: MatrixType;
}
interface FinalOmegaType extends _FinalOmegaType {
  constructor: { new (): FinalOmegaType };
}

interface _FinalPredictedNoiseType extends BaseType {
  Array: ArrayType;
}
interface FinalPredictedNoiseType extends _FinalPredictedNoiseType {
  constructor: { new (): FinalPredictedNoiseType };
}

interface _FinalStateVectorType extends BaseType {
  Array: ArrayType;
}
interface FinalStateVectorType extends _FinalStateVectorType {
  constructor: { new (): FinalStateVectorType };
}

interface _FinalThetaType extends BaseType {
  Theta: ThetaType[];
}
interface FinalThetaType extends _FinalThetaType {
  constructor: { new (): FinalThetaType };
}

export type GAP = 'true' | 'false' | 'unknown';
interface _GAP extends Primitive._string {
  content: GAP;
}

interface _GARCHPartType extends BaseType {
  constant: number;
  gp: number;
  gq: number;
  Extension?: ExtensionType[];
  ResidualSquareCoefficients: ResidualSquareCoefficientsType;
  VarianceCoefficients: VarianceCoefficientsType;
}
interface GARCHPartType extends _GARCHPartType {
  constructor: { new (): GARCHPartType };
}

interface _GARCHType extends BaseType {
  ARMAPart: ARMAPartType;
  Extension?: ExtensionType[];
  GARCHPart: GARCHPartType;
}
interface GARCHType extends _GARCHType {
  constructor: { new (): GARCHType };
}

interface _GaussianDistributionType extends BaseType {
  mean: number;
  variance: number;
  Extension?: ExtensionType[];
}
interface GaussianDistributionType extends _GaussianDistributionType {
  constructor: { new (): GaussianDistributionType };
}

interface _GaussianProcessModelType extends BaseType {
  algorithmName: string;
  functionName: MININGFUNCTION;
  isScorable: boolean;
  modelName?: string;
  optimizer?: string;
  AbsoluteExponentialKernel: AbsoluteExponentialKernelType;
  ARDSquaredExponentialKernel: ARDSquaredExponentialKernelType;
  Extension?: ExtensionType[];
  GeneralizedExponentialKernel: GeneralizedExponentialKernelType;
  LocalTransformations?: LocalTransformationsType;
  MiningSchema: MiningSchemaType;
  ModelExplanation?: ModelExplanationType;
  ModelStats?: ModelStatsType;
  ModelVerification?: ModelVerificationType;
  Output?: OutputType;
  RadialBasisKernel: RadialBasisKernelType;
  Targets?: TargetsType;
  TrainingInstances: TrainingInstancesType;
}
interface GaussianProcessModelType extends _GaussianProcessModelType {
  constructor: { new (): GaussianProcessModelType };
}

interface _GeneralizedExponentialKernelType extends BaseType {
  degree?: number;
  description?: string;
  gamma?: number;
  noiseVariance?: number;
  Extension?: ExtensionType[];
  Lambda?: LambdaType[];
}
interface GeneralizedExponentialKernelType
  extends _GeneralizedExponentialKernelType {
  constructor: { new (): GeneralizedExponentialKernelType };
}

interface _GeneralRegressionModelType extends BaseType {
  algorithmName: string;
  baselineStrataVariable: string;
  cumulativeLink: CUMULATIVELINKFUNCTION;
  distParameter: number;
  distribution: GeneralRegressionModelTypeDistributionType;
  endTimeVariable: string;
  functionName: MININGFUNCTION;
  isScorable: boolean;
  linkFunction: LINKFUNCTION;
  linkParameter: number;
  modelDF: number;
  modelName: string;
  modelType: GeneralRegressionModelTypeModelTypeType;
  offsetValue: number;
  offsetVariable: string;
  startTimeVariable: string;
  statusVariable: string;
  subjectIDVariable: string;
  targetReferenceCategory: string;
  targetVariableName: string;
  trialsValue: number;
  trialsVariable: string;
  BaseCumHazardTables?: BaseCumHazardTablesType;
  CovariateList?: CovariateListType;
  EventValues?: EventValuesType;
  Extension?: ExtensionType[];
  FactorList?: FactorListType;
  LocalTransformations?: LocalTransformationsType;
  MiningSchema: MiningSchemaType;
  ModelExplanation?: ModelExplanationType;
  ModelStats?: ModelStatsType;
  ModelVerification?: ModelVerificationType;
  Output?: OutputType;
  ParameterList: ParameterListType;
  ParamMatrix: ParamMatrixType;
  PCovMatrix?: PCovMatrixType;
  PPMatrix: PPMatrixType;
  Targets?: TargetsType;
}
interface GeneralRegressionModelType extends _GeneralRegressionModelType {
  constructor: { new (): GeneralRegressionModelType };
}

type GeneralRegressionModelTypeDistributionType =
  | 'binomial'
  | 'gamma'
  | 'igauss'
  | 'negbin'
  | 'normal'
  | 'poisson'
  | 'tweedie';
interface _GeneralRegressionModelTypeDistributionType
  extends Primitive._string {
  content: GeneralRegressionModelTypeDistributionType;
}

type GeneralRegressionModelTypeModelTypeType =
  | 'regression'
  | 'generalLinear'
  | 'multinomialLogistic'
  | 'ordinalMultinomial'
  | 'generalizedLinear'
  | 'CoxRegression';
interface _GeneralRegressionModelTypeModelTypeType extends Primitive._string {
  content: GeneralRegressionModelTypeModelTypeType;
}

interface _HeaderType extends BaseType {
  copyright: string;
  description: string;
  modelVersion: string;
  Annotation?: AnnotationType[];
  Application?: ApplicationType;
  Extension?: ExtensionType[];
  Timestamp?: TimestampType;
}
interface HeaderType extends _HeaderType {
  constructor: { new (): HeaderType };
}

interface _HVectorType extends BaseType {
  Array: ArrayType;
}
interface HVectorType extends _HVectorType {
  constructor: { new (): HVectorType };
}

type IndicesType = number[];

export type INFERENCETYPE = 'Other' | 'MCMC' | 'Exact';
interface _INFERENCETYPE extends Primitive._string {
  content: INFERENCETYPE;
}

interface _InlineTableType extends BaseType {
  Extension?: ExtensionType[];
  row?: any[];
}
interface InlineTableType extends _InlineTableType {
  constructor: { new (): InlineTableType };
}

interface _InstanceFieldsType extends BaseType {
  Extension?: ExtensionType[];
  InstanceField: InstanceFieldType[];
}
interface InstanceFieldsType extends _InstanceFieldsType {
  constructor: { new (): InstanceFieldsType };
}

interface _InstanceFieldType extends BaseType {
  column?: string;
  field: string;
  Extension?: ExtensionType[];
}
interface InstanceFieldType extends _InstanceFieldType {
  constructor: { new (): InstanceFieldType };
}

type INTEntriesType = number[];

export type INTERPOLATIONMETHOD =
  | 'none'
  | 'linear'
  | 'exponentialSpline'
  | 'cubicSpline';
interface _INTERPOLATIONMETHOD extends Primitive._string {
  content: INTERPOLATIONMETHOD;
}

interface _IntervalType extends BaseType {
  closure: IntervalTypeClosureType;
  leftMargin: number;
  rightMargin: number;
  Extension?: ExtensionType[];
}
interface IntervalType extends _IntervalType {
  constructor: { new (): IntervalType };
}

type IntervalTypeClosureType =
  | 'openClosed'
  | 'openOpen'
  | 'closedOpen'
  | 'closedClosed';
interface _IntervalTypeClosureType extends Primitive._string {
  content: IntervalTypeClosureType;
}

export type INTNUMBER = number;
type _INTNUMBER = Primitive._number;

interface _INTSparseArrayType extends BaseType {
  defaultValue?: number;
  n?: number;
  Indices?: IndicesType;
  INTEntries?: INTEntriesType;
}
interface INTSparseArrayType extends _INTSparseArrayType {
  constructor: { new (): INTSparseArrayType };
}

export type INVALIDVALUETREATMENTMETHOD =
  | 'returnInvalid'
  | 'asIs'
  | 'asMissing'
  | 'asValue';
interface _INVALIDVALUETREATMENTMETHOD extends Primitive._string {
  content: INVALIDVALUETREATMENTMETHOD;
}

interface _ItemRefType extends BaseType {
  itemRef: string;
  Extension?: ExtensionType[];
}
interface ItemRefType extends _ItemRefType {
  constructor: { new (): ItemRefType };
}

interface _ItemsetType extends BaseType {
  id: string;
  numberOfItems: number;
  support: number;
  Extension?: ExtensionType[];
  ItemRef?: ItemRefType[];
}
interface ItemsetType extends _ItemsetType {
  constructor: { new (): ItemsetType };
}

interface _ItemType extends BaseType {
  category: string;
  field: string;
  id: string;
  mappedValue: string;
  value: string;
  weight: number;
  Extension?: ExtensionType[];
}
interface ItemType extends _ItemType {
  constructor: { new (): ItemType };
}

interface _JaccardType extends BaseType {
  Extension?: ExtensionType[];
}
interface JaccardType extends _JaccardType {
  constructor: { new (): JaccardType };
}

interface _KalmanStateType extends BaseType {
  FinalOmega: FinalOmegaType;
  FinalStateVector: FinalStateVectorType;
  HVector?: HVectorType;
}
interface KalmanStateType extends _KalmanStateType {
  constructor: { new (): KalmanStateType };
}

interface _KNNInputsType extends BaseType {
  Extension?: ExtensionType[];
  KNNInput: KNNInputType[];
}
interface KNNInputsType extends _KNNInputsType {
  constructor: { new (): KNNInputsType };
}

interface _KNNInputType extends BaseType {
  compareFunction: COMPAREFUNCTION;
  field: string;
  fieldWeight: number;
  Extension?: ExtensionType[];
}
interface KNNInputType extends _KNNInputType {
  constructor: { new (): KNNInputType };
}

interface _KohonenMapType extends BaseType {
  coord1?: number;
  coord2?: number;
  coord3?: number;
  Extension?: ExtensionType[];
}
interface KohonenMapType extends _KohonenMapType {
  constructor: { new (): KohonenMapType };
}

interface _LagType extends BaseType {
  aggregate: LagTypeAggregateType;
  field: string;
  n: number;
  BlockIndicator?: BlockIndicatorType[];
  Extension?: ExtensionType[];
}
interface LagType extends _LagType {
  constructor: { new (): LagType };
}

type LagTypeAggregateType =
  | 'none'
  | 'avg'
  | 'max'
  | 'median'
  | 'min'
  | 'product'
  | 'sum'
  | 'stddev';
interface _LagTypeAggregateType extends Primitive._string {
  content: LagTypeAggregateType;
}

interface _LambdaType extends BaseType {
  Array: ArrayType;
  Extension?: ExtensionType[];
}
interface LambdaType extends _LambdaType {
  constructor: { new (): LambdaType };
}

interface _LevelType extends BaseType {
  alpha?: number;
  smoothedValue: number;
}
interface LevelType extends _LevelType {
  constructor: { new (): LevelType };
}

interface _LiftDataType extends BaseType {
  rankingQuality: number;
  targetFieldDisplayValue: string;
  targetFieldValue: string;
  Extension?: ExtensionType[];
  ModelLiftGraph: ModelLiftGraphType;
  OptimumLiftGraph?: OptimumLiftGraphType;
  RandomLiftGraph?: RandomLiftGraphType;
}
interface LiftDataType extends _LiftDataType {
  constructor: { new (): LiftDataType };
}

interface _LiftGraphType extends BaseType {
  BoundaryValueMeans?: BoundaryValueMeansType;
  BoundaryValues?: BoundaryValuesType;
  Extension?: ExtensionType[];
  XCoordinates: XCoordinatesType;
  YCoordinates: YCoordinatesType;
}
interface LiftGraphType extends _LiftGraphType {
  constructor: { new (): LiftGraphType };
}

interface _LinearKernelTypeType extends BaseType {
  description?: string;
  Extension?: ExtensionType[];
}
interface LinearKernelTypeType extends _LinearKernelTypeType {
  constructor: { new (): LinearKernelTypeType };
}

interface _LinearNormType extends BaseType {
  norm: number;
  orig: number;
  Extension?: ExtensionType[];
}
interface LinearNormType extends _LinearNormType {
  constructor: { new (): LinearNormType };
}

export type LINKFUNCTION =
  | 'cloglog'
  | 'identity'
  | 'log'
  | 'logc'
  | 'logit'
  | 'loglog'
  | 'negbin'
  | 'oddspower'
  | 'power'
  | 'probit';
interface _LINKFUNCTION extends Primitive._string {
  content: LINKFUNCTION;
}

interface _LocalTransformationsType extends BaseType {
  DerivedField?: DerivedFieldType[];
  Extension?: ExtensionType[];
}
interface LocalTransformationsType extends _LocalTransformationsType {
  constructor: { new (): LocalTransformationsType };
}

interface _LognormalDistributionForBNType extends BaseType {
  Extension?: ExtensionType[];
  Mean: MeanType;
  Variance: VarianceType;
}
interface LognormalDistributionForBNType
  extends _LognormalDistributionForBNType {
  constructor: { new (): LognormalDistributionForBNType };
}

interface _LowerType extends BaseType {
  Aggregate: AggregateType;
  Apply: ApplyType;
  Constant: ConstantType;
  Discretize: DiscretizeType;
  Extension?: ExtensionType[];
  FieldRef: FieldRefType;
  Lag: LagType;
  MapValues: MapValuesType;
  NormContinuous: NormContinuousType;
  NormDiscrete: NormDiscreteType;
  TextIndex: TextIndexType;
}
interface LowerType extends _LowerType {
  constructor: { new (): LowerType };
}

interface _MACoefficientsType extends BaseType {
  Array: ArrayType;
  Extension?: ExtensionType[];
}
interface MACoefficientsType extends _MACoefficientsType {
  constructor: { new (): MACoefficientsType };
}

interface _MapValuesType extends BaseType {
  dataType: DATATYPE;
  defaultValue: string;
  mapMissingTo: string;
  outputColumn: string;
  Extension?: ExtensionType[];
  FieldColumnPair?: FieldColumnPairType[];
  InlineTable?: InlineTableType;
  TableLocator?: TableLocatorType;
}
interface MapValuesType extends _MapValuesType {
  constructor: { new (): MapValuesType };
}

interface _MatCellType extends Primitive._string {
  col: number;
  row: number;
}
interface MatCellType extends _MatCellType {
  constructor: { new (): MatCellType };
}

interface _MatrixType extends BaseType {
  diagDefault?: number;
  kind?: MatrixTypeKindType;
  nbCols?: number;
  nbRows?: number;
  offDiagDefault?: number;
  Array?: ArrayType[];
  MatCell?: MatCellType[];
}
interface MatrixType extends _MatrixType {
  constructor: { new (): MatrixType };
}

type MatrixTypeKindType = 'diagonal' | 'symmetric' | 'any';
interface _MatrixTypeKindType extends Primitive._string {
  content: MatrixTypeKindType;
}

interface _MAType extends BaseType {
  Extension?: ExtensionType[];
  MACoefficients?: MACoefficientsType;
  Residuals?: ResidualsType;
}
interface MAType extends _MAType {
  constructor: { new (): MAType };
}

interface _MaximumLikelihoodStatType extends BaseType {
  method: MaximumLikelihoodStatTypeMethodType;
  periodDeficit: number;
  KalmanState: KalmanStateType;
  ThetaRecursionState: ThetaRecursionStateType;
}
interface MaximumLikelihoodStatType extends _MaximumLikelihoodStatType {
  constructor: { new (): MaximumLikelihoodStatType };
}

type MaximumLikelihoodStatTypeMethodType = 'kalman' | 'thetaRecursion';
interface _MaximumLikelihoodStatTypeMethodType extends Primitive._string {
  content: MaximumLikelihoodStatTypeMethodType;
}

interface _MeanClusterDistancesType extends BaseType {
  Array: ArrayType;
  Extension?: ExtensionType[];
}
interface MeanClusterDistancesType extends _MeanClusterDistancesType {
  constructor: { new (): MeanClusterDistancesType };
}

interface _MeanType extends BaseType {
  Aggregate: AggregateType;
  Apply: ApplyType;
  Constant: ConstantType;
  Discretize: DiscretizeType;
  Extension?: ExtensionType[];
  FieldRef: FieldRefType;
  Lag: LagType;
  MapValues: MapValuesType;
  NormContinuous: NormContinuousType;
  NormDiscrete: NormDiscreteType;
  TextIndex: TextIndexType;
}
interface MeanType extends _MeanType {
  constructor: { new (): MeanType };
}

interface _MeasurementMatrixType extends BaseType {
  Extension?: ExtensionType[];
  Matrix: MatrixType;
}
interface MeasurementMatrixType extends _MeasurementMatrixType {
  constructor: { new (): MeasurementMatrixType };
}

interface _MiningBuildTaskType extends BaseType {
  Extension?: ExtensionType[];
}
interface MiningBuildTaskType extends _MiningBuildTaskType {
  constructor: { new (): MiningBuildTaskType };
}

interface _MiningFieldType extends BaseType {
  highValue: number;
  importance: number;
  invalidValueReplacement: string;
  invalidValueTreatment: INVALIDVALUETREATMENTMETHOD;
  lowValue: number;
  missingValueReplacement: string;
  missingValueTreatment: MISSINGVALUETREATMENTMETHOD;
  name: string;
  optype: OPTYPE;
  outliers: OUTLIERTREATMENTMETHOD;
  usageType: FIELDUSAGETYPE;
  Extension?: ExtensionType[];
}
interface MiningFieldType extends _MiningFieldType {
  constructor: { new (): MiningFieldType };
}

export type MININGFUNCTION =
  | 'associationRules'
  | 'sequences'
  | 'classification'
  | 'regression'
  | 'clustering'
  | 'timeSeries'
  | 'mixed';
interface _MININGFUNCTION extends Primitive._string {
  content: MININGFUNCTION;
}

interface _MiningModelType extends BaseType {
  algorithmName?: string;
  functionName: MININGFUNCTION;
  isScorable: boolean;
  modelName?: string;
  DecisionTree?: DecisionTreeType[];
  Extension?: ExtensionType[];
  LocalTransformations?: LocalTransformationsType;
  MiningSchema: MiningSchemaType;
  ModelExplanation?: ModelExplanationType;
  ModelStats?: ModelStatsType;
  ModelVerification?: ModelVerificationType;
  Output?: OutputType;
  Regression?: RegressionType[];
  Segmentation?: SegmentationType;
  Targets?: TargetsType;
}
interface MiningModelType extends _MiningModelType {
  constructor: { new (): MiningModelType };
}

interface _MiningSchemaType extends BaseType {
  Extension?: ExtensionType[];
  MiningField: MiningFieldType[];
}
interface MiningSchemaType extends _MiningSchemaType {
  constructor: { new (): MiningSchemaType };
}

interface _MinkowskiType extends BaseType {
  pParameter: number;
  Extension?: ExtensionType[];
}
interface MinkowskiType extends _MinkowskiType {
  constructor: { new (): MinkowskiType };
}

export type MISSINGPREDICTIONTREATMENT =
  | 'returnMissing'
  | 'skipSegment'
  | 'continue';
interface _MISSINGPREDICTIONTREATMENT extends Primitive._string {
  content: MISSINGPREDICTIONTREATMENT;
}

export type MISSINGVALUESTRATEGY =
  | 'lastPrediction'
  | 'nullPrediction'
  | 'defaultChild'
  | 'weightedConfidence'
  | 'aggregateNodes'
  | 'none';
interface _MISSINGVALUESTRATEGY extends Primitive._string {
  content: MISSINGVALUESTRATEGY;
}

export type MISSINGVALUETREATMENTMETHOD =
  | 'asIs'
  | 'asMean'
  | 'asMode'
  | 'asMedian'
  | 'asValue'
  | 'returnInvalid';
interface _MISSINGVALUETREATMENTMETHOD extends Primitive._string {
  content: MISSINGVALUETREATMENTMETHOD;
}

interface _MissingValueWeightsType extends BaseType {
  Array: ArrayType;
  Extension?: ExtensionType[];
}
interface MissingValueWeightsType extends _MissingValueWeightsType {
  constructor: { new (): MissingValueWeightsType };
}

interface _ModelExplanationType extends BaseType {
  ClusteringModelQuality?: ClusteringModelQualityType[];
  Correlations?: CorrelationsType;
  Extension?: ExtensionType[];
  PredictiveModelQuality?: PredictiveModelQualityType[];
}
interface ModelExplanationType extends _ModelExplanationType {
  constructor: { new (): ModelExplanationType };
}

interface _ModelLiftGraphType extends BaseType {
  Extension?: ExtensionType[];
  LiftGraph: LiftGraphType;
}
interface ModelLiftGraphType extends _ModelLiftGraphType {
  constructor: { new (): ModelLiftGraphType };
}

interface _ModelStatsType extends BaseType {
  Extension?: ExtensionType[];
  MultivariateStats?: MultivariateStatsType[];
  UnivariateStats?: UnivariateStatsType[];
}
interface ModelStatsType extends _ModelStatsType {
  constructor: { new (): ModelStatsType };
}

interface _ModelVerificationType extends BaseType {
  fieldCount?: number;
  recordCount?: number;
  Extension?: ExtensionType[];
  InlineTable: InlineTableType;
  VerificationFields: VerificationFieldsType;
}
interface ModelVerificationType extends _ModelVerificationType {
  constructor: { new (): ModelVerificationType };
}

export type MULTIPLEMODELMETHOD =
  | 'majorityVote'
  | 'weightedMajorityVote'
  | 'average'
  | 'weightedAverage'
  | 'median'
  | 'weightedMedian'
  | 'max'
  | 'sum'
  | 'weightedSum'
  | 'selectFirst'
  | 'selectAll'
  | 'modelChain';
interface _MULTIPLEMODELMETHOD extends Primitive._string {
  content: MULTIPLEMODELMETHOD;
}

interface _MultivariateStatsType extends BaseType {
  targetCategory?: string;
  Extension?: ExtensionType[];
  MultivariateStat: MultivariateStatType[];
}
interface MultivariateStatsType extends _MultivariateStatsType {
  constructor: { new (): MultivariateStatsType };
}

interface _MultivariateStatType extends BaseType {
  category: string;
  chiSquareValue: number;
  confidenceLevel: number;
  confidenceLowerBound: number;
  confidenceUpperBound: number;
  dF: number;
  exponent: number;
  fStatistic: number;
  importance: number;
  isIntercept: boolean;
  name: string;
  pValueAlpha: number;
  pValueFinal: number;
  pValueInitial: number;
  stdError: number;
  tValue: number;
  Extension?: ExtensionType[];
}
interface MultivariateStatType extends _MultivariateStatType {
  constructor: { new (): MultivariateStatType };
}

interface _NaiveBayesModelType extends BaseType {
  algorithmName: string;
  functionName: MININGFUNCTION;
  isScorable: boolean;
  modelName: string;
  threshold: number;
  BayesInputs: BayesInputsType;
  BayesOutput: BayesOutputType;
  Extension?: ExtensionType[];
  LocalTransformations?: LocalTransformationsType;
  MiningSchema: MiningSchemaType;
  ModelExplanation?: ModelExplanationType;
  ModelStats?: ModelStatsType;
  ModelVerification?: ModelVerificationType;
  Output?: OutputType;
  Targets?: TargetsType;
}
interface NaiveBayesModelType extends _NaiveBayesModelType {
  constructor: { new (): NaiveBayesModelType };
}

interface _NearestNeighborModelType extends BaseType {
  algorithmName: string;
  categoricalScoringMethod: CATSCORINGMETHOD;
  continuousScoringMethod: CONTSCORINGMETHOD;
  functionName: MININGFUNCTION;
  instanceIdVariable: string;
  isScorable: boolean;
  modelName: string;
  numberOfNeighbors: number;
  threshold: number;
  ComparisonMeasure: ComparisonMeasureType;
  Extension?: ExtensionType[];
  KNNInputs: KNNInputsType;
  LocalTransformations?: LocalTransformationsType;
  MiningSchema: MiningSchemaType;
  ModelExplanation?: ModelExplanationType;
  ModelStats?: ModelStatsType;
  ModelVerification?: ModelVerificationType;
  Output?: OutputType;
  Targets?: TargetsType;
  TrainingInstances: TrainingInstancesType;
}
interface NearestNeighborModelType extends _NearestNeighborModelType {
  constructor: { new (): NearestNeighborModelType };
}

interface _NeuralInputsType extends BaseType {
  numberOfInputs: number;
  Extension?: ExtensionType[];
  NeuralInput: NeuralInputType[];
}
interface NeuralInputsType extends _NeuralInputsType {
  constructor: { new (): NeuralInputsType };
}

interface _NeuralInputType extends BaseType {
  id: string;
  DerivedField: DerivedFieldType;
  Extension?: ExtensionType[];
}
interface NeuralInputType extends _NeuralInputType {
  constructor: { new (): NeuralInputType };
}

interface _NeuralLayerType extends BaseType {
  activationFunction: ACTIVATIONFUNCTION;
  altitude: number;
  normalizationMethod: NNNORMALIZATIONMETHOD;
  numberOfNeurons: number;
  threshold: number;
  width: number;
  Extension?: ExtensionType[];
  Neuron: NeuronType[];
}
interface NeuralLayerType extends _NeuralLayerType {
  constructor: { new (): NeuralLayerType };
}

interface _NeuralNetworkType extends BaseType {
  activationFunction: ACTIVATIONFUNCTION;
  algorithmName: string;
  altitude: number;
  functionName: MININGFUNCTION;
  isScorable: boolean;
  modelName: string;
  normalizationMethod: NNNORMALIZATIONMETHOD;
  numberOfLayers: number;
  threshold: number;
  width: number;
  Extension?: ExtensionType[];
  LocalTransformations?: LocalTransformationsType;
  MiningSchema: MiningSchemaType;
  ModelExplanation?: ModelExplanationType;
  ModelStats?: ModelStatsType;
  ModelVerification?: ModelVerificationType;
  NeuralInputs: NeuralInputsType;
  NeuralLayer: NeuralLayerType[];
  NeuralOutputs?: NeuralOutputsType;
  Output?: OutputType;
  Targets?: TargetsType;
}
interface NeuralNetworkType extends _NeuralNetworkType {
  constructor: { new (): NeuralNetworkType };
}

interface _NeuralOutputsType extends BaseType {
  numberOfOutputs: number;
  Extension?: ExtensionType[];
  NeuralOutput: NeuralOutputType[];
}
interface NeuralOutputsType extends _NeuralOutputsType {
  constructor: { new (): NeuralOutputsType };
}

interface _NeuralOutputType extends BaseType {
  outputNeuron: string;
  DerivedField: DerivedFieldType;
  Extension?: ExtensionType[];
}
interface NeuralOutputType extends _NeuralOutputType {
  constructor: { new (): NeuralOutputType };
}

interface _NeuronType extends BaseType {
  altitude: number;
  bias: number;
  id: string;
  width: number;
  Con: ConType[];
  Extension?: ExtensionType[];
}
interface NeuronType extends _NeuronType {
  constructor: { new (): NeuronType };
}

export type NNNEURONID = string;
type _NNNEURONID = Primitive._string;

export type NNNEURONIDREF = string;
type _NNNEURONIDREF = Primitive._string;

export type NNNORMALIZATIONMETHOD = 'none' | 'simplemax' | 'softmax';
interface _NNNORMALIZATIONMETHOD extends Primitive._string {
  content: NNNORMALIZATIONMETHOD;
}

interface _NodeType extends BaseType {
  defaultChild: string;
  id: string;
  recordCount: number;
  score: string;
  CompoundPredicate: CompoundPredicateType;
  DecisionTree: DecisionTreeType;
  Extension?: ExtensionType[];
  False: FalseType;
  Node?: NodeType[];
  Partition?: PartitionType;
  Regression: RegressionType;
  ScoreDistribution?: ScoreDistributionType[];
  SimplePredicate: SimplePredicateType;
  SimpleSetPredicate: SimpleSetPredicateType;
  True: TrueType;
}
interface NodeType extends _NodeType {
  constructor: { new (): NodeType };
}

interface _NonseasonalComponentType extends BaseType {
  d: number;
  p: number;
  q: number;
  AR?: ARType;
  Extension?: ExtensionType[];
  MA?: MAType;
}
interface NonseasonalComponentType extends _NonseasonalComponentType {
  constructor: { new (): NonseasonalComponentType };
}

interface _NonseasonalFactorType extends BaseType {
  difference: number;
  maximumOrder: number;
  Array: ArrayType;
  Extension?: ExtensionType[];
}
interface NonseasonalFactorType extends _NonseasonalFactorType {
  constructor: { new (): NonseasonalFactorType };
}

interface _NormalDistributionForBNType extends BaseType {
  Extension?: ExtensionType[];
  Mean: MeanType;
  Variance: VarianceType;
}
interface NormalDistributionForBNType extends _NormalDistributionForBNType {
  constructor: { new (): NormalDistributionForBNType };
}

interface _NormContinuousType extends BaseType {
  field: string;
  mapMissingTo: number;
  outliers: OUTLIERTREATMENTMETHOD;
  Extension?: ExtensionType[];
  LinearNorm: LinearNormType[];
}
interface NormContinuousType extends _NormContinuousType {
  constructor: { new (): NormContinuousType };
}

interface _NormDiscreteType extends BaseType {
  field: string;
  mapMissingTo: number;
  value: string;
  Extension?: ExtensionType[];
}
interface NormDiscreteType extends _NormDiscreteType {
  constructor: { new (): NormDiscreteType };
}

export type NOTRUECHILDSTRATEGY =
  | 'returnNullPrediction'
  | 'returnLastPrediction';
interface _NOTRUECHILDSTRATEGY extends Primitive._string {
  content: NOTRUECHILDSTRATEGY;
}

export type NUMBER = number;
type _NUMBER = Primitive._number;

interface _NumeratorType extends BaseType {
  Extension?: ExtensionType[];
  NonseasonalFactor?: NonseasonalFactorType;
  SeasonalFactor?: SeasonalFactorType;
}
interface NumeratorType extends _NumeratorType {
  constructor: { new (): NumeratorType };
}

interface _NumericInfoType extends BaseType {
  interQuartileRange: number;
  maximum: number;
  mean: number;
  median: number;
  minimum: number;
  standardDeviation: number;
  Extension?: ExtensionType[];
  Quantile?: QuantileType[];
}
interface NumericInfoType extends _NumericInfoType {
  constructor: { new (): NumericInfoType };
}

interface _NumericPredictorType extends BaseType {
  coefficient: number;
  exponent: number;
  name: string;
  Extension?: ExtensionType[];
}
interface NumericPredictorType extends _NumericPredictorType {
  constructor: { new (): NumericPredictorType };
}

interface _OptimumLiftGraphType extends BaseType {
  Extension?: ExtensionType[];
  LiftGraph: LiftGraphType;
}
interface OptimumLiftGraphType extends _OptimumLiftGraphType {
  constructor: { new (): OptimumLiftGraphType };
}

export type OPTYPE = 'categorical' | 'ordinal' | 'continuous';
interface _OPTYPE extends Primitive._string {
  content: OPTYPE;
}

interface _OutlierEffectType extends BaseType {
  dampingCoefficient?: number;
  magnitude: number;
  startTime: number;
  type: OutlierEffectTypeTypeType;
  Extension?: ExtensionType[];
}
interface OutlierEffectType extends _OutlierEffectType {
  constructor: { new (): OutlierEffectType };
}

type OutlierEffectTypeTypeType =
  | 'additive'
  | 'level'
  | 'transient'
  | 'seasonalAdditive'
  | 'trend'
  | 'innovational';
interface _OutlierEffectTypeTypeType extends Primitive._string {
  content: OutlierEffectTypeTypeType;
}

export type OUTLIERTREATMENTMETHOD =
  | 'asIs'
  | 'asMissingValues'
  | 'asExtremeValues';
interface _OUTLIERTREATMENTMETHOD extends Primitive._string {
  content: OUTLIERTREATMENTMETHOD;
}

interface _OutputFieldType extends BaseType {
  algorithm: OutputFieldTypeAlgorithmType;
  dataType: DATATYPE;
  displayName: string;
  feature: RESULTFEATURE;
  isFinalResult: boolean;
  isMultiValued: string;
  name: string;
  optype: OPTYPE;
  rank: number;
  rankBasis: OutputFieldTypeRankBasisType;
  rankOrder: OutputFieldTypeRankOrderType;
  ruleFeature: RULEFEATURE;
  segmentId: string;
  targetField: string;
  value: string;
  Aggregate?: AggregateType;
  Apply?: ApplyType;
  Constant?: ConstantType;
  Decisions?: DecisionsType;
  Discretize?: DiscretizeType;
  Extension?: ExtensionType[];
  FieldRef?: FieldRefType;
  Lag?: LagType;
  MapValues?: MapValuesType;
  NormContinuous?: NormContinuousType;
  NormDiscrete?: NormDiscreteType;
  TextIndex?: TextIndexType;
  Value?: ValueType[];
}
interface OutputFieldType extends _OutputFieldType {
  constructor: { new (): OutputFieldType };
}

type OutputFieldTypeAlgorithmType =
  | 'recommendation'
  | 'exclusiveRecommendation'
  | 'ruleAssociation';
interface _OutputFieldTypeAlgorithmType extends Primitive._string {
  content: OutputFieldTypeAlgorithmType;
}

type OutputFieldTypeRankBasisType =
  | 'confidence'
  | 'support'
  | 'lift'
  | 'leverage'
  | 'affinity';
interface _OutputFieldTypeRankBasisType extends Primitive._string {
  content: OutputFieldTypeRankBasisType;
}

type OutputFieldTypeRankOrderType = 'descending' | 'ascending';
interface _OutputFieldTypeRankOrderType extends Primitive._string {
  content: OutputFieldTypeRankOrderType;
}

interface _OutputType extends BaseType {
  Extension?: ExtensionType[];
  OutputField: OutputFieldType[];
}
interface OutputType extends _OutputType {
  constructor: { new (): OutputType };
}

interface _PairCountsType extends BaseType {
  value: string;
  Extension?: ExtensionType[];
  TargetValueCounts: TargetValueCountsType;
}
interface PairCountsType extends _PairCountsType {
  constructor: { new (): PairCountsType };
}

interface _ParameterFieldType extends BaseType {
  dataType: DATATYPE;
  displayName: string;
  name: string;
  optype: OPTYPE;
}
interface ParameterFieldType extends _ParameterFieldType {
  constructor: { new (): ParameterFieldType };
}

interface _ParameterListType extends BaseType {
  Extension?: ExtensionType[];
  Parameter?: ParameterType[];
}
interface ParameterListType extends _ParameterListType {
  constructor: { new (): ParameterListType };
}

interface _ParameterType extends BaseType {
  label: string;
  name: string;
  referencePoint: number;
  Extension?: ExtensionType[];
}
interface ParameterType extends _ParameterType {
  constructor: { new (): ParameterType };
}

interface _ParamMatrixType extends BaseType {
  Extension?: ExtensionType[];
  PCell?: PCellType[];
}
interface ParamMatrixType extends _ParamMatrixType {
  constructor: { new (): ParamMatrixType };
}

interface _ParentValueType extends BaseType {
  parent: string;
  value: string;
  Extension?: ExtensionType[];
}
interface ParentValueType extends _ParentValueType {
  constructor: { new (): ParentValueType };
}

interface _PartitionFieldStatsType extends BaseType {
  field: string;
  weighted: PartitionFieldStatsTypeWeightedType;
  Array?: ArrayType[];
  Counts?: CountsType;
  Extension?: ExtensionType[];
  NumericInfo?: NumericInfoType;
}
interface PartitionFieldStatsType extends _PartitionFieldStatsType {
  constructor: { new (): PartitionFieldStatsType };
}

type PartitionFieldStatsTypeWeightedType = '0' | '1';
interface _PartitionFieldStatsTypeWeightedType extends Primitive._string {
  content: PartitionFieldStatsTypeWeightedType;
}

interface _PartitionType extends BaseType {
  name: string;
  size: number;
  Extension?: ExtensionType[];
  PartitionFieldStats?: PartitionFieldStatsType[];
}
interface PartitionType extends _PartitionType {
  constructor: { new (): PartitionType };
}

interface _PastVariancesType extends BaseType {
  Array: ArrayType;
  Extension?: ExtensionType[];
}
interface PastVariancesType extends _PastVariancesType {
  constructor: { new (): PastVariancesType };
}

interface _PCellType extends BaseType {
  beta: number;
  df: number;
  parameterName: string;
  targetCategory: string;
  Extension?: ExtensionType[];
}
interface PCellType extends _PCellType {
  constructor: { new (): PCellType };
}

interface _PCovCellType extends BaseType {
  pCol: string;
  pRow: string;
  targetCategory: string;
  tCol: string;
  tRow: string;
  value: number;
  Extension?: ExtensionType[];
}
interface PCovCellType extends _PCovCellType {
  constructor: { new (): PCovCellType };
}

interface _PCovMatrixType extends BaseType {
  type: PCovMatrixTypeTypeType;
  Extension?: ExtensionType[];
  PCovCell: PCovCellType[];
}
interface PCovMatrixType extends _PCovMatrixType {
  constructor: { new (): PCovMatrixType };
}

type PCovMatrixTypeTypeType = 'model' | 'robust';
interface _PCovMatrixTypeTypeType extends Primitive._string {
  content: PCovMatrixTypeTypeType;
}

export type PERCENTAGENUMBER = number;
type _PERCENTAGENUMBER = Primitive._number;

interface _PMMLType extends BaseType {
  version: string;
  AnomalyDetectionModel?: AnomalyDetectionModelType[];
  AssociationModel?: AssociationModelType[];
  BaselineModel?: BaselineModelType[];
  BayesianNetworkModel?: BayesianNetworkModelType[];
  ClusteringModel?: ClusteringModelType[];
  DataDictionary: DataDictionaryType;
  Extension?: ExtensionType[];
  GaussianProcessModel?: GaussianProcessModelType[];
  GeneralRegressionModel?: GeneralRegressionModelType[];
  Header: HeaderType;
  MiningBuildTask?: MiningBuildTaskType;
  MiningModel?: MiningModelType[];
  NaiveBayesModel?: NaiveBayesModelType[];
  NearestNeighborModel?: NearestNeighborModelType[];
  NeuralNetwork?: NeuralNetworkType[];
  RegressionModel?: RegressionModelType[];
  RuleSetModel?: RuleSetModelType[];
  Scorecard?: ScorecardType[];
  SequenceModel?: SequenceModelType[];
  SupportVectorMachineModel?: SupportVectorMachineModelType[];
  TextModel?: TextModelType[];
  TimeSeriesModel?: TimeSeriesModelType[];
  TransformationDictionary?: TransformationDictionaryType;
  TreeModel?: TreeModelType[];
}
interface PMMLType extends _PMMLType {
  constructor: { new (): PMMLType };
}

interface _PoissonDistributionType extends BaseType {
  mean: number;
  Extension?: ExtensionType[];
}
interface PoissonDistributionType extends _PoissonDistributionType {
  constructor: { new (): PoissonDistributionType };
}

interface _PolynomialKernelTypeType extends BaseType {
  coef0?: number;
  degree?: number;
  description?: string;
  gamma?: number;
  Extension?: ExtensionType[];
}
interface PolynomialKernelTypeType extends _PolynomialKernelTypeType {
  constructor: { new (): PolynomialKernelTypeType };
}

interface _PPCellType extends BaseType {
  parameterName: string;
  predictorName: string;
  targetCategory: string;
  value: string;
  Extension?: ExtensionType[];
}
interface PPCellType extends _PPCellType {
  constructor: { new (): PPCellType };
}

interface _PPMatrixType extends BaseType {
  Extension?: ExtensionType[];
  PPCell?: PPCellType[];
}
interface PPMatrixType extends _PPMatrixType {
  constructor: { new (): PPMatrixType };
}

interface _PredictiveModelQualityType extends BaseType {
  adjRsquared?: number;
  AIC?: number;
  AICc?: number;
  BIC?: number;
  dataName?: string;
  dataUsage: PredictiveModelQualityTypeDataUsageType;
  degreesOfFreedom?: number;
  fStatistic?: number;
  meanAbsoluteError?: number;
  meanError?: number;
  meanSquaredError?: number;
  numOfPredictors?: number;
  numOfRecords?: number;
  numOfRecordsWeighted?: number;
  rSquared?: number;
  rootMeanSquaredError?: number;
  sumSquaredError?: number;
  sumSquaredRegression?: number;
  targetField: string;
  ConfusionMatrix?: ConfusionMatrixType;
  Extension?: ExtensionType[];
  LiftData?: LiftDataType[];
  ROC?: ROCType;
}
interface PredictiveModelQualityType extends _PredictiveModelQualityType {
  constructor: { new (): PredictiveModelQualityType };
}

type PredictiveModelQualityTypeDataUsageType =
  | 'training'
  | 'test'
  | 'validation';
interface _PredictiveModelQualityTypeDataUsageType extends Primitive._string {
  content: PredictiveModelQualityTypeDataUsageType;
}

interface _PredictorTermType extends BaseType {
  coefficient: number;
  name: string;
  Extension?: ExtensionType[];
  FieldRef: FieldRefType[];
}
interface PredictorTermType extends _PredictorTermType {
  constructor: { new (): PredictorTermType };
}

interface _PredictorType extends BaseType {
  contrastMatrixType: string;
  name: string;
  Categories?: CategoriesType;
  Extension?: ExtensionType[];
  Matrix?: MatrixType;
}
interface PredictorType extends _PredictorType {
  constructor: { new (): PredictorType };
}

export type PROBNUMBER = number;
type _PROBNUMBER = Primitive._number;

interface _PsiVectorType extends BaseType {
  targetField: string;
  variance: string;
  Array: ArrayType;
  Extension?: ExtensionType[];
}
interface PsiVectorType extends _PsiVectorType {
  constructor: { new (): PsiVectorType };
}

interface _QuantileType extends BaseType {
  quantileLimit: number;
  quantileValue: number;
  Extension?: ExtensionType[];
}
interface QuantileType extends _QuantileType {
  constructor: { new (): QuantileType };
}

interface _RadialBasisKernelType extends BaseType {
  description?: string;
  gamma?: number;
  lambda?: number;
  noiseVariance?: number;
  Extension?: ExtensionType[];
}
interface RadialBasisKernelType extends _RadialBasisKernelType {
  constructor: { new (): RadialBasisKernelType };
}

interface _RadialBasisKernelTypeType extends BaseType {
  description?: string;
  gamma?: number;
  Extension?: ExtensionType[];
}
interface RadialBasisKernelTypeType extends _RadialBasisKernelTypeType {
  constructor: { new (): RadialBasisKernelTypeType };
}

interface _RandomLiftGraphType extends BaseType {
  Extension?: ExtensionType[];
  LiftGraph: LiftGraphType;
}
interface RandomLiftGraphType extends _RandomLiftGraphType {
  constructor: { new (): RandomLiftGraphType };
}

type REALEntriesType = number[];

export type REALNUMBER = number;
type _REALNUMBER = Primitive._number;

interface _REALSparseArrayType extends BaseType {
  defaultValue?: number;
  n?: number;
  Indices?: IndicesType;
  REALEntries?: REALEntriesType;
}
interface REALSparseArrayType extends _REALSparseArrayType {
  constructor: { new (): REALSparseArrayType };
}

interface _RegressionModelType extends BaseType {
  algorithmName: string;
  functionName: MININGFUNCTION;
  isScorable: boolean;
  modelName: string;
  modelType?: RegressionModelTypeModelTypeType;
  normalizationMethod: REGRESSIONNORMALIZATIONMETHOD;
  targetFieldName?: string;
  Extension?: ExtensionType[];
  LocalTransformations?: LocalTransformationsType;
  MiningSchema: MiningSchemaType;
  ModelExplanation?: ModelExplanationType;
  ModelStats?: ModelStatsType;
  ModelVerification?: ModelVerificationType;
  Output?: OutputType;
  RegressionTable: RegressionTableType[];
  Targets?: TargetsType;
}
interface RegressionModelType extends _RegressionModelType {
  constructor: { new (): RegressionModelType };
}

type RegressionModelTypeModelTypeType =
  | 'linearRegression'
  | 'stepwisePolynomialRegression'
  | 'logisticRegression';
interface _RegressionModelTypeModelTypeType extends Primitive._string {
  content: RegressionModelTypeModelTypeType;
}

export type REGRESSIONNORMALIZATIONMETHOD =
  | 'none'
  | 'simplemax'
  | 'softmax'
  | 'logit'
  | 'probit'
  | 'cloglog'
  | 'exp'
  | 'loglog'
  | 'cauchit';
interface _REGRESSIONNORMALIZATIONMETHOD extends Primitive._string {
  content: REGRESSIONNORMALIZATIONMETHOD;
}

interface _RegressionTableType extends BaseType {
  intercept: number;
  targetCategory: string;
  CategoricalPredictor?: CategoricalPredictorType[];
  Extension?: ExtensionType[];
  NumericPredictor?: NumericPredictorType[];
  PredictorTerm?: PredictorTermType[];
}
interface RegressionTableType extends _RegressionTableType {
  constructor: { new (): RegressionTableType };
}

interface _RegressionType extends BaseType {
  algorithmName: string;
  functionName: MININGFUNCTION;
  modelName: string;
  normalizationMethod: REGRESSIONNORMALIZATIONMETHOD;
  Extension?: ExtensionType[];
  LocalTransformations?: LocalTransformationsType;
  ModelStats?: ModelStatsType;
  Output?: OutputType;
  RegressionTable: RegressionTableType[];
  ResultField?: ResultFieldType[];
  Targets?: TargetsType;
}
interface RegressionType extends _RegressionType {
  constructor: { new (): RegressionType };
}

interface _RegressorValuesType extends BaseType {
  Extension?: ExtensionType[];
  TimeSeries?: TimeSeriesType;
  TransferFunctionValues?: TransferFunctionValuesType;
  TrendCoefficients?: TrendCoefficientsType;
}
interface RegressorValuesType extends _RegressorValuesType {
  constructor: { new (): RegressorValuesType };
}

interface _ResidualSquareCoefficientsType extends BaseType {
  Extension?: ExtensionType[];
  MACoefficients?: MACoefficientsType;
  Residuals?: ResidualsType;
}
interface ResidualSquareCoefficientsType
  extends _ResidualSquareCoefficientsType {
  constructor: { new (): ResidualSquareCoefficientsType };
}

interface _ResidualsType extends BaseType {
  Array: ArrayType;
  Extension?: ExtensionType[];
}
interface ResidualsType extends _ResidualsType {
  constructor: { new (): ResidualsType };
}

export type RESULTFEATURE =
  | 'predictedValue'
  | 'predictedDisplayValue'
  | 'transformedValue'
  | 'decision'
  | 'probability'
  | 'affinity'
  | 'residual'
  | 'standardError'
  | 'standardDeviation'
  | 'clusterId'
  | 'clusterAffinity'
  | 'entityId'
  | 'entityAffinity'
  | 'warning'
  | 'ruleValue'
  | 'reasonCode'
  | 'antecedent'
  | 'consequent'
  | 'rule'
  | 'ruleId'
  | 'confidence'
  | 'support'
  | 'lift'
  | 'leverage';
interface _RESULTFEATURE extends Primitive._string {
  content: RESULTFEATURE;
}

interface _ResultFieldType extends BaseType {
  dataType: DATATYPE;
  displayName: string;
  feature: RESULTFEATURE;
  name: string;
  optype: OPTYPE;
  value: string;
  Extension?: ExtensionType[];
}
interface ResultFieldType extends _ResultFieldType {
  constructor: { new (): ResultFieldType };
}

interface _ROCGraphType extends BaseType {
  BoundaryValues?: BoundaryValuesType;
  Extension?: ExtensionType[];
  XCoordinates: XCoordinatesType;
  YCoordinates: YCoordinatesType;
}
interface ROCGraphType extends _ROCGraphType {
  constructor: { new (): ROCGraphType };
}

interface _ROCType extends BaseType {
  negativeTargetFieldDisplayValue: string;
  negativeTargetFieldValue: string;
  positiveTargetFieldDisplayValue: string;
  positiveTargetFieldValue: string;
  Extension?: ExtensionType[];
  ROCGraph: ROCGraphType;
}
interface ROCType extends _ROCType {
  constructor: { new (): ROCType };
}

type RowType = any;
type _RowType = Primitive._any;

export type RULEFEATURE =
  | 'antecedent'
  | 'consequent'
  | 'rule'
  | 'ruleId'
  | 'confidence'
  | 'support'
  | 'lift'
  | 'leverage'
  | 'affinity';
interface _RULEFEATURE extends Primitive._string {
  content: RULEFEATURE;
}

interface _RuleSelectionMethodType extends BaseType {
  criterion: RuleSelectionMethodTypeCriterionType;
  Extension?: ExtensionType[];
}
interface RuleSelectionMethodType extends _RuleSelectionMethodType {
  constructor: { new (): RuleSelectionMethodType };
}

type RuleSelectionMethodTypeCriterionType =
  | 'weightedSum'
  | 'weightedMax'
  | 'firstHit';
interface _RuleSelectionMethodTypeCriterionType extends Primitive._string {
  content: RuleSelectionMethodTypeCriterionType;
}

interface _RuleSetModelType extends BaseType {
  algorithmName?: string;
  functionName: MININGFUNCTION;
  isScorable: boolean;
  modelName?: string;
  Extension?: ExtensionType[];
  LocalTransformations?: LocalTransformationsType;
  MiningSchema: MiningSchemaType;
  ModelExplanation?: ModelExplanationType;
  ModelStats?: ModelStatsType;
  ModelVerification?: ModelVerificationType;
  Output?: OutputType;
  RuleSet: RuleSetType;
  Targets?: TargetsType;
}
interface RuleSetModelType extends _RuleSetModelType {
  constructor: { new (): RuleSetModelType };
}

interface _RuleSetType extends BaseType {
  defaultConfidence?: number;
  defaultScore?: string;
  nbCorrect?: number;
  recordCount?: number;
  CompoundRule?: CompoundRuleType[];
  Extension?: ExtensionType[];
  RuleSelectionMethod: RuleSelectionMethodType[];
  ScoreDistribution?: ScoreDistributionType[];
  SimpleRule?: SimpleRuleType[];
}
interface RuleSetType extends _RuleSetType {
  constructor: { new (): RuleSetType };
}

interface _ScorecardType extends BaseType {
  algorithmName: string;
  baselineMethod: ScorecardTypeBaselineMethodType;
  baselineScore: number;
  functionName: MININGFUNCTION;
  initialScore: number;
  isScorable: boolean;
  modelName: string;
  reasonCodeAlgorithm: ScorecardTypeReasonCodeAlgorithmType;
  useReasonCodes: boolean;
  Characteristics: CharacteristicsType;
  Extension?: ExtensionType[];
  LocalTransformations?: LocalTransformationsType;
  MiningSchema: MiningSchemaType;
  ModelExplanation?: ModelExplanationType;
  ModelStats?: ModelStatsType;
  ModelVerification?: ModelVerificationType;
  Output?: OutputType;
  Targets?: TargetsType;
}
interface ScorecardType extends _ScorecardType {
  constructor: { new (): ScorecardType };
}

type ScorecardTypeBaselineMethodType =
  | 'max'
  | 'min'
  | 'mean'
  | 'neutral'
  | 'other';
interface _ScorecardTypeBaselineMethodType extends Primitive._string {
  content: ScorecardTypeBaselineMethodType;
}

type ScorecardTypeReasonCodeAlgorithmType = 'pointsAbove' | 'pointsBelow';
interface _ScorecardTypeReasonCodeAlgorithmType extends Primitive._string {
  content: ScorecardTypeReasonCodeAlgorithmType;
}

interface _ScoreDistributionType extends BaseType {
  confidence: number;
  probability: number;
  recordCount: number;
  value: string;
  Extension?: ExtensionType[];
}
interface ScoreDistributionType extends _ScoreDistributionType {
  constructor: { new (): ScoreDistributionType };
}

interface _SeasonalComponentType extends BaseType {
  D: number;
  P: number;
  period: number;
  Q: number;
  AR?: ARType;
  Extension?: ExtensionType[];
  MA?: MAType;
}
interface SeasonalComponentType extends _SeasonalComponentType {
  constructor: { new (): SeasonalComponentType };
}

interface _SeasonalFactorType extends BaseType {
  difference: number;
  maximumOrder: number;
  Array: ArrayType;
  Extension?: ExtensionType[];
}
interface SeasonalFactorType extends _SeasonalFactorType {
  constructor: { new (): SeasonalFactorType };
}

interface _Seasonality_ExpoSmoothType extends BaseType {
  delta?: number;
  period: number;
  phase?: number;
  type: Seasonality_ExpoSmoothTypeTypeType;
  unit?: string;
  Array: ArrayType;
}
interface Seasonality_ExpoSmoothType extends _Seasonality_ExpoSmoothType {
  constructor: { new (): Seasonality_ExpoSmoothType };
}

type Seasonality_ExpoSmoothTypeTypeType = 'additive' | 'multiplicative';
interface _Seasonality_ExpoSmoothTypeTypeType extends Primitive._string {
  content: Seasonality_ExpoSmoothTypeTypeType;
}

interface _SegmentationType extends BaseType {
  missingPredictionTreatment: MISSINGPREDICTIONTREATMENT;
  missingThreshold: number;
  multipleModelMethod: MULTIPLEMODELMETHOD;
  Extension?: ExtensionType[];
  Segment: SegmentType[];
}
interface SegmentationType extends _SegmentationType {
  constructor: { new (): SegmentationType };
}

interface _SegmentType extends BaseType {
  id?: string;
  weight?: number;
  AnomalyDetectionModel: AnomalyDetectionModelType;
  AssociationModel: AssociationModelType;
  BaselineModel: BaselineModelType;
  BayesianNetworkModel: BayesianNetworkModelType;
  ClusteringModel: ClusteringModelType;
  CompoundPredicate: CompoundPredicateType;
  Extension?: ExtensionType[];
  False: FalseType;
  GaussianProcessModel: GaussianProcessModelType;
  GeneralRegressionModel: GeneralRegressionModelType;
  MiningModel: MiningModelType;
  NaiveBayesModel: NaiveBayesModelType;
  NearestNeighborModel: NearestNeighborModelType;
  NeuralNetwork: NeuralNetworkType;
  RegressionModel: RegressionModelType;
  RuleSetModel: RuleSetModelType;
  Scorecard: ScorecardType;
  SequenceModel: SequenceModelType;
  SimplePredicate: SimplePredicateType;
  SimpleSetPredicate: SimpleSetPredicateType;
  SupportVectorMachineModel: SupportVectorMachineModelType;
  TextModel: TextModelType;
  TimeSeriesModel: TimeSeriesModelType;
  TreeModel: TreeModelType;
  True: TrueType;
  VariableWeight?: VariableWeightType;
}
interface SegmentType extends _SegmentType {
  constructor: { new (): SegmentType };
}

interface _SequenceModelType extends BaseType {
  algorithmName: string;
  avgNumberOfItemsPerTransaction: number;
  avgNumberOfTAsPerTAGroup: number;
  functionName: MININGFUNCTION;
  isScorable: boolean;
  maxNumberOfItemsPerTransaction: number;
  maxNumberOfTAsPerTAGroup: number;
  modelName: string;
  numberOfTransactionGroups: number;
  numberOfTransactions: number;
  Constraints?: ConstraintsType;
  Extension?: ExtensionType[];
  Item?: ItemType[];
  Itemset?: ItemsetType[];
  LocalTransformations?: LocalTransformationsType;
  MiningSchema: MiningSchemaType;
  ModelStats?: ModelStatsType;
  Sequence: SequenceType[];
  SequenceRule?: SequenceRuleType[];
  SetPredicate?: SetPredicateType[];
}
interface SequenceModelType extends _SequenceModelType {
  constructor: { new (): SequenceModelType };
}

interface _SequenceReferenceType extends BaseType {
  seqId: string;
  Extension?: ExtensionType[];
}
interface SequenceReferenceType extends _SequenceReferenceType {
  constructor: { new (): SequenceReferenceType };
}

interface _SequenceRuleType extends BaseType {
  confidence: number;
  id: string;
  lift: number;
  numberOfSets: number;
  occurrence: number;
  support: number;
  AntecedentSequence: AntecedentSequenceType;
  ConsequentSequence: ConsequentSequenceType;
  Delimiter: DelimiterType;
  Extension?: ExtensionType[];
  Time?: TimeType[];
}
interface SequenceRuleType extends _SequenceRuleType {
  constructor: { new (): SequenceRuleType };
}

interface _SequenceType extends BaseType {
  id: string;
  numberOfSets: number;
  occurrence: number;
  support: number;
  Delimiter?: DelimiterType[];
  Extension?: ExtensionType[];
  SetReference: SetReferenceType[];
  Time?: TimeType[];
}
interface SequenceType extends _SequenceType {
  constructor: { new (): SequenceType };
}

interface _SetPredicateType extends BaseType {
  field: string;
  id: string;
  operator: string;
  Array: ArrayType;
  Extension?: ExtensionType[];
}
interface SetPredicateType extends _SetPredicateType {
  constructor: { new (): SetPredicateType };
}

interface _SetReferenceType extends BaseType {
  setId: string;
  Extension?: ExtensionType[];
}
interface SetReferenceType extends _SetReferenceType {
  constructor: { new (): SetReferenceType };
}

interface _SigmoidKernelTypeType extends BaseType {
  coef0?: number;
  description?: string;
  gamma?: number;
  Extension?: ExtensionType[];
}
interface SigmoidKernelTypeType extends _SigmoidKernelTypeType {
  constructor: { new (): SigmoidKernelTypeType };
}

interface _SimpleMatchingType extends BaseType {
  Extension?: ExtensionType[];
}
interface SimpleMatchingType extends _SimpleMatchingType {
  constructor: { new (): SimpleMatchingType };
}

interface _SimplePredicateType extends BaseType {
  field: string;
  operator: SimplePredicateTypeOperatorType;
  value: string;
  Extension?: ExtensionType[];
}
interface SimplePredicateType extends _SimplePredicateType {
  constructor: { new (): SimplePredicateType };
}

type SimplePredicateTypeOperatorType =
  | 'equal'
  | 'notEqual'
  | 'lessThan'
  | 'lessOrEqual'
  | 'greaterThan'
  | 'greaterOrEqual'
  | 'isMissing'
  | 'isNotMissing';
interface _SimplePredicateTypeOperatorType extends Primitive._string {
  content: SimplePredicateTypeOperatorType;
}

interface _SimpleRuleType extends BaseType {
  confidence?: number;
  id?: string;
  nbCorrect?: number;
  recordCount?: number;
  score: string;
  weight?: number;
  CompoundPredicate: CompoundPredicateType;
  Extension?: ExtensionType[];
  False: FalseType;
  ScoreDistribution?: ScoreDistributionType[];
  SimplePredicate: SimplePredicateType;
  SimpleSetPredicate: SimpleSetPredicateType;
  True: TrueType;
}
interface SimpleRuleType extends _SimpleRuleType {
  constructor: { new (): SimpleRuleType };
}

interface _SimpleSetPredicateType extends BaseType {
  booleanOperator: SimpleSetPredicateTypeBooleanOperatorType;
  field: string;
  Array: ArrayType;
  Extension?: ExtensionType[];
}
interface SimpleSetPredicateType extends _SimpleSetPredicateType {
  constructor: { new (): SimpleSetPredicateType };
}

type SimpleSetPredicateTypeBooleanOperatorType = 'isIn' | 'isNotIn';
interface _SimpleSetPredicateTypeBooleanOperatorType extends Primitive._string {
  content: SimpleSetPredicateTypeBooleanOperatorType;
}

interface _SquaredEuclideanType extends BaseType {
  Extension?: ExtensionType[];
}
interface SquaredEuclideanType extends _SquaredEuclideanType {
  constructor: { new (): SquaredEuclideanType };
}

interface _StateSpaceModelType extends BaseType {
  intercept: number;
  variance: number;
  DynamicRegressor?: DynamicRegressorType[];
  Extension?: ExtensionType[];
  MeasurementMatrix?: MeasurementMatrixType;
  PsiVector?: PsiVectorType;
  StateVector?: StateVectorType;
  TransitionMatrix?: TransitionMatrixType;
}
interface StateSpaceModelType extends _StateSpaceModelType {
  constructor: { new (): StateSpaceModelType };
}

interface _StateVectorType extends BaseType {
  Array: ArrayType;
  Extension?: ExtensionType[];
}
interface StateVectorType extends _StateVectorType {
  constructor: { new (): StateVectorType };
}

interface _SupportVectorMachineModelType extends BaseType {
  algorithmName?: string;
  classificationMethod?: SVMCLASSIFICATIONMETHOD;
  functionName: MININGFUNCTION;
  isScorable: boolean;
  maxWins: boolean;
  modelName?: string;
  svmRepresentation?: SVMREPRESENTATION;
  threshold?: number;
  Extension?: ExtensionType[];
  LinearKernelType: LinearKernelTypeType;
  LocalTransformations?: LocalTransformationsType;
  MiningSchema: MiningSchemaType;
  ModelExplanation?: ModelExplanationType;
  ModelStats?: ModelStatsType;
  ModelVerification?: ModelVerificationType;
  Output?: OutputType;
  PolynomialKernelType: PolynomialKernelTypeType;
  RadialBasisKernelType: RadialBasisKernelTypeType;
  SigmoidKernelType: SigmoidKernelTypeType;
  SupportVectorMachine: SupportVectorMachineType[];
  Targets?: TargetsType;
  VectorDictionary: VectorDictionaryType;
}
interface SupportVectorMachineModelType extends _SupportVectorMachineModelType {
  constructor: { new (): SupportVectorMachineModelType };
}

interface _SupportVectorMachineType extends BaseType {
  alternateTargetCategory?: string;
  targetCategory?: string;
  threshold?: number;
  Coefficients: CoefficientsType;
  Extension?: ExtensionType[];
  SupportVectors?: SupportVectorsType;
}
interface SupportVectorMachineType extends _SupportVectorMachineType {
  constructor: { new (): SupportVectorMachineType };
}

interface _SupportVectorsType extends BaseType {
  numberOfAttributes?: number;
  numberOfSupportVectors?: number;
  Extension?: ExtensionType[];
  SupportVector: SupportVectorType[];
}
interface SupportVectorsType extends _SupportVectorsType {
  constructor: { new (): SupportVectorsType };
}

interface _SupportVectorType extends BaseType {
  vectorId: string;
  Extension?: ExtensionType[];
}
interface SupportVectorType extends _SupportVectorType {
  constructor: { new (): SupportVectorType };
}

export type SVMCLASSIFICATIONMETHOD = 'OneAgainstAll' | 'OneAgainstOne';
interface _SVMCLASSIFICATIONMETHOD extends Primitive._string {
  content: SVMCLASSIFICATIONMETHOD;
}

export type SVMREPRESENTATION = 'SupportVectors' | 'Coefficients';
interface _SVMREPRESENTATION extends Primitive._string {
  content: SVMREPRESENTATION;
}

interface _TableLocatorType extends BaseType {
  Extension?: ExtensionType[];
}
interface TableLocatorType extends _TableLocatorType {
  constructor: { new (): TableLocatorType };
}

interface _TanimotoType extends BaseType {
  Extension?: ExtensionType[];
}
interface TanimotoType extends _TanimotoType {
  constructor: { new (): TanimotoType };
}

interface _TargetsType extends BaseType {
  Extension?: ExtensionType[];
  Target: TargetType[];
}
interface TargetsType extends _TargetsType {
  constructor: { new (): TargetsType };
}

interface _TargetType extends BaseType {
  castInteger: TargetTypeCastIntegerType;
  field: string;
  max: number;
  min: number;
  optype: OPTYPE;
  rescaleConstant: number;
  rescaleFactor: number;
  Extension?: ExtensionType[];
  TargetValue?: TargetValueType[];
}
interface TargetType extends _TargetType {
  constructor: { new (): TargetType };
}

type TargetTypeCastIntegerType = 'round' | 'ceiling' | 'floor';
interface _TargetTypeCastIntegerType extends Primitive._string {
  content: TargetTypeCastIntegerType;
}

interface _TargetValueCountsType extends BaseType {
  Extension?: ExtensionType[];
  TargetValueCount: TargetValueCountType[];
}
interface TargetValueCountsType extends _TargetValueCountsType {
  constructor: { new (): TargetValueCountsType };
}

interface _TargetValueCountType extends BaseType {
  count: number;
  value: string;
  Extension?: ExtensionType[];
}
interface TargetValueCountType extends _TargetValueCountType {
  constructor: { new (): TargetValueCountType };
}

interface _TargetValueStatsType extends BaseType {
  Extension?: ExtensionType[];
  TargetValueStat: TargetValueStatType[];
}
interface TargetValueStatsType extends _TargetValueStatsType {
  constructor: { new (): TargetValueStatsType };
}

interface _TargetValueStatType extends BaseType {
  value: string;
  AnyDistribution: AnyDistributionType;
  Extension?: ExtensionType[];
  GaussianDistribution: GaussianDistributionType;
  PoissonDistribution: PoissonDistributionType;
  UniformDistribution: UniformDistributionType;
}
interface TargetValueStatType extends _TargetValueStatType {
  constructor: { new (): TargetValueStatType };
}

interface _TargetValueType extends BaseType {
  defaultValue: number;
  displayValue: string;
  priorProbability: number;
  value: string;
  Extension?: ExtensionType[];
  Partition?: PartitionType;
}
interface TargetValueType extends _TargetValueType {
  constructor: { new (): TargetValueType };
}

interface _TaxonomyType extends BaseType {
  name: string;
  ChildParent: ChildParentType[];
  Extension?: ExtensionType[];
}
interface TaxonomyType extends _TaxonomyType {
  constructor: { new (): TaxonomyType };
}

interface _TestDistributionsType extends BaseType {
  field: string;
  normalizationScheme?: string;
  resetValue?: number;
  testStatistic: BASELINETESTSTATISTIC;
  weightField?: string;
  windowSize?: number;
  Alternate?: AlternateType;
  Baseline: BaselineType;
  Extension?: ExtensionType[];
}
interface TestDistributionsType extends _TestDistributionsType {
  constructor: { new (): TestDistributionsType };
}

interface _TextCorpusType extends BaseType {
  Extension?: ExtensionType[];
  TextDocument?: TextDocumentType[];
}
interface TextCorpusType extends _TextCorpusType {
  constructor: { new (): TextCorpusType };
}

interface _TextDictionaryType extends BaseType {
  Array: ArrayType;
  Extension?: ExtensionType[];
  Taxonomy?: TaxonomyType;
}
interface TextDictionaryType extends _TextDictionaryType {
  constructor: { new (): TextDictionaryType };
}

interface _TextDocumentType extends BaseType {
  file?: string;
  id: string;
  length?: number;
  name?: string;
  Extension?: ExtensionType[];
}
interface TextDocumentType extends _TextDocumentType {
  constructor: { new (): TextDocumentType };
}

interface _TextIndexNormalizationType extends BaseType {
  inField: string;
  isCaseSensitive: boolean;
  maxLevenshteinDistance: number;
  outField: string;
  recursive: boolean;
  regexField: string;
  tokenize: boolean;
  wordSeparatorCharacterRE: string;
  Extension?: ExtensionType[];
  InlineTable?: InlineTableType;
  TableLocator?: TableLocatorType;
}
interface TextIndexNormalizationType extends _TextIndexNormalizationType {
  constructor: { new (): TextIndexNormalizationType };
}

interface _TextIndexType extends BaseType {
  countHits: TextIndexTypeCountHitsType;
  isCaseSensitive: boolean;
  localTermWeights: TextIndexTypeLocalTermWeightsType;
  maxLevenshteinDistance: number;
  textField: string;
  tokenize: boolean;
  wordSeparatorCharacterRE: string;
  Aggregate: AggregateType;
  Apply: ApplyType;
  Constant: ConstantType;
  Discretize: DiscretizeType;
  Extension?: ExtensionType[];
  FieldRef: FieldRefType;
  Lag: LagType;
  MapValues: MapValuesType;
  NormContinuous: NormContinuousType;
  NormDiscrete: NormDiscreteType;
  TextIndex: TextIndexType;
  TextIndexNormalization?: TextIndexNormalizationType[];
}
interface TextIndexType extends _TextIndexType {
  constructor: { new (): TextIndexType };
}

type TextIndexTypeCountHitsType = 'allHits' | 'bestHits';
interface _TextIndexTypeCountHitsType extends Primitive._string {
  content: TextIndexTypeCountHitsType;
}

type TextIndexTypeLocalTermWeightsType =
  | 'termFrequency'
  | 'binary'
  | 'logarithmic'
  | 'augmentedNormalizedTermFrequency';
interface _TextIndexTypeLocalTermWeightsType extends Primitive._string {
  content: TextIndexTypeLocalTermWeightsType;
}

interface _TextModelNormalizationType extends BaseType {
  documentNormalization: TextModelNormalizationTypeDocumentNormalizationType;
  globalTermWeights: TextModelNormalizationTypeGlobalTermWeightsType;
  localTermWeights: TextModelNormalizationTypeLocalTermWeightsType;
  Extension?: ExtensionType[];
}
interface TextModelNormalizationType extends _TextModelNormalizationType {
  constructor: { new (): TextModelNormalizationType };
}

type TextModelNormalizationTypeDocumentNormalizationType = 'none' | 'cosine';
interface _TextModelNormalizationTypeDocumentNormalizationType
  extends Primitive._string {
  content: TextModelNormalizationTypeDocumentNormalizationType;
}

type TextModelNormalizationTypeGlobalTermWeightsType =
  | 'inverseDocumentFrequency'
  | 'none'
  | 'GFIDF'
  | 'normal'
  | 'probabilisticInverse';
interface _TextModelNormalizationTypeGlobalTermWeightsType
  extends Primitive._string {
  content: TextModelNormalizationTypeGlobalTermWeightsType;
}

type TextModelNormalizationTypeLocalTermWeightsType =
  | 'termFrequency'
  | 'binary'
  | 'logarithmic'
  | 'augmentedNormalizedTermFrequency';
interface _TextModelNormalizationTypeLocalTermWeightsType
  extends Primitive._string {
  content: TextModelNormalizationTypeLocalTermWeightsType;
}

interface _TextModelSimiliarityType extends BaseType {
  similarityType: TextModelSimiliarityTypeSimilarityTypeType;
  Extension?: ExtensionType[];
}
interface TextModelSimiliarityType extends _TextModelSimiliarityType {
  constructor: { new (): TextModelSimiliarityType };
}

type TextModelSimiliarityTypeSimilarityTypeType = 'euclidean' | 'cosine';
interface _TextModelSimiliarityTypeSimilarityTypeType
  extends Primitive._string {
  content: TextModelSimiliarityTypeSimilarityTypeType;
}

interface _TextModelType extends BaseType {
  algorithmName: string;
  functionName: MININGFUNCTION;
  isScorable: boolean;
  modelName: string;
  numberOfDocuments: number;
  numberOfTerms: number;
  DocumentTermMatrix: DocumentTermMatrixType;
  Extension?: ExtensionType[];
  LocalTransformations?: LocalTransformationsType;
  MiningSchema: MiningSchemaType;
  ModelExplanation?: ModelExplanationType;
  ModelStats?: ModelStatsType;
  ModelVerification?: ModelVerificationType;
  Output?: OutputType;
  Targets?: TargetsType;
  TextCorpus: TextCorpusType;
  TextDictionary: TextDictionaryType;
  TextModelNormalization?: TextModelNormalizationType;
  TextModelSimiliarity?: TextModelSimiliarityType;
}
interface TextModelType extends _TextModelType {
  constructor: { new (): TextModelType };
}

interface _ThetaRecursionStateType extends BaseType {
  FinalNoise: FinalNoiseType;
  FinalNu: FinalNuType;
  FinalPredictedNoise: FinalPredictedNoiseType;
  FinalTheta: FinalThetaType;
}
interface ThetaRecursionStateType extends _ThetaRecursionStateType {
  constructor: { new (): ThetaRecursionStateType };
}

interface _ThetaType extends BaseType {
  i: number;
  j: number;
  theta: number;
}
interface ThetaType extends _ThetaType {
  constructor: { new (): ThetaType };
}

export type TIMEANCHOR =
  | 'dateTimeMillisecondsSince[0]'
  | 'dateTimeMillisecondsSince[1960]'
  | 'dateTimeMillisecondsSince[1970]'
  | 'dateTimeMillisecondsSince[1980]'
  | 'dateTimeSecondsSince[0]'
  | 'dateTimeSecondsSince[1960]'
  | 'dateTimeSecondsSince[1970]'
  | 'dateTimeSecondsSince[1980]'
  | 'dateDaysSince[0]'
  | 'dateDaysSince[1960]'
  | 'dateDaysSince[1970]'
  | 'dateDaysSince[1980]'
  | 'dateMonthsSince[0]'
  | 'dateMonthsSince[1960]'
  | 'dateMonthsSince[1970]'
  | 'dateMonthsSince[1980]'
  | 'dateYearsSince[0]';
interface _TIMEANCHOR extends Primitive._string {
  content: TIMEANCHOR;
}

interface _TimeAnchorType extends BaseType {
  displayName?: string;
  offset: number;
  stepsize: number;
  type: TIMEANCHOR;
  TimeCycle?: TimeCycleType[];
  TimeException?: TimeExceptionType[];
}
interface TimeAnchorType extends _TimeAnchorType {
  constructor: { new (): TimeAnchorType };
}

interface _TimeCycleType extends BaseType {
  displayName?: string;
  length: number;
  type: VALIDTIMESPEC;
  Array?: ArrayType;
}
interface TimeCycleType extends _TimeCycleType {
  constructor: { new (): TimeCycleType };
}

interface _TimeExceptionType extends BaseType {
  count: number;
  type: TIMEEXCEPTIONTYPE;
  Array: ArrayType;
}
interface TimeExceptionType extends _TimeExceptionType {
  constructor: { new (): TimeExceptionType };
}

export type TIMEEXCEPTIONTYPE = 'exclude' | 'include';
interface _TIMEEXCEPTIONTYPE extends Primitive._string {
  content: TIMEEXCEPTIONTYPE;
}

export type TIMESERIESALGORITHM =
  | 'ARIMA'
  | 'ExponentialSmoothing'
  | 'SeasonalTrendDecomposition'
  | 'SpectralAnalysis'
  | 'StateSpaceModel'
  | 'GARCH';
interface _TIMESERIESALGORITHM extends Primitive._string {
  content: TIMESERIESALGORITHM;
}

interface _TimeSeriesModelType extends BaseType {
  algorithmName?: string;
  bestFit: TIMESERIESALGORITHM;
  functionName: MININGFUNCTION;
  isScorable: boolean;
  modelName?: string;
  ARIMA?: ARIMAType;
  ExponentialSmoothing?: ExponentialSmoothingType;
  Extension?: ExtensionType[];
  GARCH?: GARCHType;
  LocalTransformations?: LocalTransformationsType;
  MiningSchema: MiningSchemaType;
  ModelExplanation?: ModelExplanationType;
  ModelStats?: ModelStatsType;
  ModelVerification?: ModelVerificationType;
  Output?: OutputType;
  StateSpaceModel?: StateSpaceModelType;
  TimeSeries?: TimeSeriesType[];
}
interface TimeSeriesModelType extends _TimeSeriesModelType {
  constructor: { new (): TimeSeriesModelType };
}

interface _TimeSeriesType extends BaseType {
  endTime: number;
  field: string;
  interpolationMethod: INTERPOLATIONMETHOD;
  startTime: number;
  usage: TIMESERIESUSAGE;
  TimeAnchor?: TimeAnchorType;
  TimeValue: TimeValueType[];
}
interface TimeSeriesType extends _TimeSeriesType {
  constructor: { new (): TimeSeriesType };
}

export type TIMESERIESUSAGE = 'original' | 'logical' | 'prediction';
interface _TIMESERIESUSAGE extends Primitive._string {
  content: TIMESERIESUSAGE;
}

interface _TimestampType extends BaseType {
  Extension?: ExtensionType[];
}
interface TimestampType extends _TimestampType {
  constructor: { new (): TimestampType };
}

interface _TimeType extends BaseType {
  max: number;
  mean: number;
  min: number;
  standardDeviation: number;
  Extension?: ExtensionType[];
}
interface TimeType extends _TimeType {
  constructor: { new (): TimeType };
}

interface _TimeValueType extends BaseType {
  index?: number;
  standardError?: number;
  time?: number;
  value: number;
  Timestamp?: TimestampType;
}
interface TimeValueType extends _TimeValueType {
  constructor: { new (): TimeValueType };
}

interface _TrainingInstancesType extends BaseType {
  fieldCount?: number;
  isTransformed: boolean;
  recordCount?: number;
  Extension?: ExtensionType[];
  InlineTable: InlineTableType;
  InstanceFields: InstanceFieldsType;
  TableLocator: TableLocatorType;
}
interface TrainingInstancesType extends _TrainingInstancesType {
  constructor: { new (): TrainingInstancesType };
}

interface _TransferFunctionValuesType extends BaseType {
  Array: ArrayType;
}
interface TransferFunctionValuesType extends _TransferFunctionValuesType {
  constructor: { new (): TransferFunctionValuesType };
}

interface _TransformationDictionaryType extends BaseType {
  DefineFunction?: DefineFunctionType[];
  DerivedField?: DerivedFieldType[];
  Extension?: ExtensionType[];
}
interface TransformationDictionaryType extends _TransformationDictionaryType {
  constructor: { new (): TransformationDictionaryType };
}

interface _TransitionMatrixType extends BaseType {
  Extension?: ExtensionType[];
  Matrix: MatrixType;
}
interface TransitionMatrixType extends _TransitionMatrixType {
  constructor: { new (): TransitionMatrixType };
}

interface _TreeModelType extends BaseType {
  algorithmName: string;
  functionName: MININGFUNCTION;
  isScorable: boolean;
  missingValuePenalty: number;
  missingValueStrategy: MISSINGVALUESTRATEGY;
  modelName: string;
  noTrueChildStrategy: NOTRUECHILDSTRATEGY;
  splitCharacteristic: TreeModelTypeSplitCharacteristicType;
  Extension?: ExtensionType[];
  LocalTransformations?: LocalTransformationsType;
  MiningSchema: MiningSchemaType;
  ModelExplanation?: ModelExplanationType;
  ModelStats?: ModelStatsType;
  ModelVerification?: ModelVerificationType;
  Node: NodeType;
  Output?: OutputType;
  Targets?: TargetsType;
}
interface TreeModelType extends _TreeModelType {
  constructor: { new (): TreeModelType };
}

type TreeModelTypeSplitCharacteristicType = 'binarySplit' | 'multiSplit';
interface _TreeModelTypeSplitCharacteristicType extends Primitive._string {
  content: TreeModelTypeSplitCharacteristicType;
}

interface _Trend_ExpoSmoothType extends BaseType {
  gamma?: number;
  phi?: number;
  smoothedValue?: number;
  trend: Trend_ExpoSmoothTypeTrendType;
  Array?: ArrayType;
}
interface Trend_ExpoSmoothType extends _Trend_ExpoSmoothType {
  constructor: { new (): Trend_ExpoSmoothType };
}

type Trend_ExpoSmoothTypeTrendType =
  | 'additive'
  | 'damped_additive'
  | 'multiplicative'
  | 'damped_multiplicative'
  | 'polynomial_exponential';
interface _Trend_ExpoSmoothTypeTrendType extends Primitive._string {
  content: Trend_ExpoSmoothTypeTrendType;
}

interface _TrendCoefficientsType extends BaseType {
  Extension?: ExtensionType[];
  REALSparseArray?: REALSparseArrayType;
}
interface TrendCoefficientsType extends _TrendCoefficientsType {
  constructor: { new (): TrendCoefficientsType };
}

interface _TriangularDistributionForBNType extends BaseType {
  Extension?: ExtensionType[];
  Lower: LowerType;
  Mean: MeanType;
  Upper: UpperType;
}
interface TriangularDistributionForBNType
  extends _TriangularDistributionForBNType {
  constructor: { new (): TriangularDistributionForBNType };
}

interface _TrueType extends BaseType {
  Extension?: ExtensionType[];
}
interface TrueType extends _TrueType {
  constructor: { new (): TrueType };
}

interface _UniformDistributionForBNType extends BaseType {
  Extension?: ExtensionType[];
  Lower: LowerType;
  Upper: UpperType;
}
interface UniformDistributionForBNType extends _UniformDistributionForBNType {
  constructor: { new (): UniformDistributionForBNType };
}

interface _UniformDistributionType extends BaseType {
  lower: number;
  upper: number;
  Extension?: ExtensionType[];
}
interface UniformDistributionType extends _UniformDistributionType {
  constructor: { new (): UniformDistributionType };
}

interface _UnivariateStatsType extends BaseType {
  field: string;
  weighted: UnivariateStatsTypeWeightedType;
  Anova?: AnovaType;
  ContStats?: ContStatsType;
  Counts?: CountsType;
  DiscrStats?: DiscrStatsType;
  Extension?: ExtensionType[];
  NumericInfo?: NumericInfoType;
}
interface UnivariateStatsType extends _UnivariateStatsType {
  constructor: { new (): UnivariateStatsType };
}

type UnivariateStatsTypeWeightedType = '0' | '1';
interface _UnivariateStatsTypeWeightedType extends Primitive._string {
  content: UnivariateStatsTypeWeightedType;
}

interface _UpperType extends BaseType {
  Aggregate: AggregateType;
  Apply: ApplyType;
  Constant: ConstantType;
  Discretize: DiscretizeType;
  Extension?: ExtensionType[];
  FieldRef: FieldRefType;
  Lag: LagType;
  MapValues: MapValuesType;
  NormContinuous: NormContinuousType;
  NormDiscrete: NormDiscreteType;
  TextIndex: TextIndexType;
}
interface UpperType extends _UpperType {
  constructor: { new (): UpperType };
}

export type VALIDTIMESPEC =
  | 'includeAll'
  | 'includeFromTo'
  | 'excludeFromTo'
  | 'includeSet'
  | 'excludeSet';
interface _VALIDTIMESPEC extends Primitive._string {
  content: VALIDTIMESPEC;
}

interface _ValueProbabilityType extends BaseType {
  probability: number;
  value: string;
  Extension?: ExtensionType[];
}
interface ValueProbabilityType extends _ValueProbabilityType {
  constructor: { new (): ValueProbabilityType };
}

interface _ValueType extends BaseType {
  displayValue: string;
  property: ValueTypePropertyType;
  value: string;
  Extension?: ExtensionType[];
}
interface ValueType extends _ValueType {
  constructor: { new (): ValueType };
}

type ValueTypePropertyType = 'valid' | 'invalid' | 'missing';
interface _ValueTypePropertyType extends Primitive._string {
  content: ValueTypePropertyType;
}

interface _VariableWeightType extends BaseType {
  field: string;
  Extension?: ExtensionType[];
}
interface VariableWeightType extends _VariableWeightType {
  constructor: { new (): VariableWeightType };
}

interface _VarianceCoefficientsType extends BaseType {
  Extension?: ExtensionType[];
  MACoefficients?: MACoefficientsType;
  PastVariances?: PastVariancesType;
}
interface VarianceCoefficientsType extends _VarianceCoefficientsType {
  constructor: { new (): VarianceCoefficientsType };
}

interface _VarianceType extends BaseType {
  Aggregate: AggregateType;
  Apply: ApplyType;
  Constant: ConstantType;
  Discretize: DiscretizeType;
  Extension?: ExtensionType[];
  FieldRef: FieldRefType;
  Lag: LagType;
  MapValues: MapValuesType;
  NormContinuous: NormContinuousType;
  NormDiscrete: NormDiscreteType;
  TextIndex: TextIndexType;
}
interface VarianceType extends _VarianceType {
  constructor: { new (): VarianceType };
}

interface _VectorDictionaryType extends BaseType {
  numberOfVectors?: number;
  Extension?: ExtensionType[];
  VectorFields: VectorFieldsType;
  VectorInstance?: VectorInstanceType[];
}
interface VectorDictionaryType extends _VectorDictionaryType {
  constructor: { new (): VectorDictionaryType };
}

interface _VectorFieldsType extends BaseType {
  numberOfFields?: number;
  CategoricalPredictor: CategoricalPredictorType[];
  Extension?: ExtensionType[];
  FieldRef: FieldRefType[];
}
interface VectorFieldsType extends _VectorFieldsType {
  constructor: { new (): VectorFieldsType };
}

export type VECTORID = string;
type _VECTORID = Primitive._string;

interface _VectorInstanceType extends BaseType {
  id: string;
  Array: ArrayType;
  Extension?: ExtensionType[];
  REALSparseArray: REALSparseArrayType;
}
interface VectorInstanceType extends _VectorInstanceType {
  constructor: { new (): VectorInstanceType };
}

interface _VerificationFieldsType extends BaseType {
  Extension?: ExtensionType[];
  VerificationField: VerificationFieldType[];
}
interface VerificationFieldsType extends _VerificationFieldsType {
  constructor: { new (): VerificationFieldsType };
}

interface _VerificationFieldType extends BaseType {
  column?: string;
  field: string;
  precision: number;
  zeroThreshold: number;
  Extension?: ExtensionType[];
}
interface VerificationFieldType extends _VerificationFieldType {
  constructor: { new (): VerificationFieldType };
}

interface _XCoordinatesType extends BaseType {
  Array: ArrayType;
  Extension?: ExtensionType[];
}
interface XCoordinatesType extends _XCoordinatesType {
  constructor: { new (): XCoordinatesType };
}

interface _YCoordinatesType extends BaseType {
  Array: ArrayType;
  Extension?: ExtensionType[];
}
interface YCoordinatesType extends _YCoordinatesType {
  constructor: { new (): YCoordinatesType };
}

export interface document extends BaseType {
  AbsoluteExponentialKernel: AbsoluteExponentialKernelType;
  Aggregate: AggregateType;
  Alternate: AlternateType;
  Annotation: AnnotationType;
  AnomalyDetectionModel: AnomalyDetectionModelType;
  Anova: AnovaType;
  AnovaRow: AnovaRowType;
  AntecedentSequence: AntecedentSequenceType;
  AnyDistribution: AnyDistributionType;
  Application: ApplicationType;
  Apply: ApplyType;
  AR: ARType;
  ARDSquaredExponentialKernel: ARDSquaredExponentialKernelType;
  ARIMA: ARIMAType;
  ARMAPart: ARMAPartType;
  Array: ArrayType;
  AssociationModel: AssociationModelType;
  AssociationRule: AssociationRuleType;
  Attribute: AttributeType;
  BaseCumHazardTables: BaseCumHazardTablesType;
  Baseline: BaselineType;
  BaselineCell: BaselineCellType;
  BaselineModel: BaselineModelType;
  BaselineStratum: BaselineStratumType;
  BayesianNetworkModel: BayesianNetworkModelType;
  BayesianNetworkNodes: BayesianNetworkNodesType;
  BayesInput: BayesInputType;
  BayesInputs: BayesInputsType;
  BayesOutput: BayesOutputType;
  binarySimilarity: BinarySimilarityType;
  BlockIndicator: BlockIndicatorType;
  BoundaryValueMeans: BoundaryValueMeansType;
  BoundaryValues: BoundaryValuesType;
  CategoricalPredictor: CategoricalPredictorType;
  Categories: CategoriesType;
  Category: CategoryType;
  Characteristic: CharacteristicType;
  Characteristics: CharacteristicsType;
  chebychev: ChebychevType;
  ChildParent: ChildParentType;
  cityBlock: CityBlockType;
  ClassLabels: ClassLabelsType;
  Cluster: ClusterType;
  ClusteringField: ClusteringFieldType;
  ClusteringModel: ClusteringModelType;
  ClusteringModelQuality: ClusteringModelQualityType;
  Coefficient: CoefficientType;
  Coefficients: CoefficientsType;
  ComparisonMeasure: ComparisonMeasureType;
  Comparisons: ComparisonsType;
  ComplexPartialScore: ComplexPartialScoreType;
  CompoundPredicate: CompoundPredicateType;
  CompoundRule: CompoundRuleType;
  Con: ConType;
  ConfusionMatrix: ConfusionMatrixType;
  ConsequentSequence: ConsequentSequenceType;
  Constant: ConstantType;
  Constraints: ConstraintsType;
  ContinuousConditionalProbability: ContinuousConditionalProbabilityType;
  ContinuousDistribution: ContinuousDistributionType;
  ContinuousNode: ContinuousNodeType;
  ContStats: ContStatsType;
  CorrelationFields: CorrelationFieldsType;
  CorrelationMethods: CorrelationMethodsType;
  Correlations: CorrelationsType;
  CorrelationValues: CorrelationValuesType;
  Counts: CountsType;
  CountTable: COUNTTABLETYPE;
  Covariances: CovariancesType;
  CovariateList: CovariateListType;
  DataDictionary: DataDictionaryType;
  DataField: DataFieldType;
  Decision: DecisionType;
  Decisions: DecisionsType;
  DecisionTree: DecisionTreeType;
  DefineFunction: DefineFunctionType;
  Delimiter: DelimiterType;
  Denominator: DenominatorType;
  DerivedField: DerivedFieldType;
  DiscreteConditionalProbability: DiscreteConditionalProbabilityType;
  DiscreteNode: DiscreteNodeType;
  Discretize: DiscretizeType;
  DiscretizeBin: DiscretizeBinType;
  DiscrStats: DiscrStatsType;
  DocumentTermMatrix: DocumentTermMatrixType;
  DynamicRegressor: DynamicRegressorType;
  euclidean: EuclideanType;
  EventValues: EventValuesType;
  ExponentialSmoothing: ExponentialSmoothingType;
  Extension: ExtensionType;
  FactorList: FactorListType;
  False: FalseType;
  FieldColumnPair: FieldColumnPairType;
  FieldRef: FieldRefType;
  FieldValue: FieldValueType;
  FieldValueCount: FieldValueCountType;
  FinalNoise: FinalNoiseType;
  FinalNu: FinalNuType;
  FinalOmega: FinalOmegaType;
  FinalPredictedNoise: FinalPredictedNoiseType;
  FinalStateVector: FinalStateVectorType;
  FinalTheta: FinalThetaType;
  GARCH: GARCHType;
  GARCHPart: GARCHPartType;
  GaussianDistribution: GaussianDistributionType;
  GaussianProcessModel: GaussianProcessModelType;
  GeneralizedExponentialKernel: GeneralizedExponentialKernelType;
  GeneralRegressionModel: GeneralRegressionModelType;
  Header: HeaderType;
  HVector: HVectorType;
  Indices: IndicesType;
  InlineTable: InlineTableType;
  InstanceField: InstanceFieldType;
  InstanceFields: InstanceFieldsType;
  INTEntries: INTEntriesType;
  INTSparseArray: INTSparseArrayType;
  Interval: IntervalType;
  Item: ItemType;
  ItemRef: ItemRefType;
  Itemset: ItemsetType;
  jaccard: JaccardType;
  KalmanState: KalmanStateType;
  KNNInput: KNNInputType;
  KNNInputs: KNNInputsType;
  KohonenMap: KohonenMapType;
  Lag: LagType;
  Lambda: LambdaType;
  Level: LevelType;
  LiftData: LiftDataType;
  LiftGraph: LiftGraphType;
  LinearKernelType: LinearKernelTypeType;
  LinearNorm: LinearNormType;
  LocalTransformations: LocalTransformationsType;
  LognormalDistributionForBN: LognormalDistributionForBNType;
  Lower: LowerType;
  MA: MAType;
  MACoefficients: MACoefficientsType;
  MapValues: MapValuesType;
  MatCell: MatCellType;
  Matrix: MatrixType;
  MaximumLikelihoodStat: MaximumLikelihoodStatType;
  Mean: MeanType;
  MeanClusterDistances: MeanClusterDistancesType;
  MeasurementMatrix: MeasurementMatrixType;
  MiningBuildTask: MiningBuildTaskType;
  MiningField: MiningFieldType;
  MiningModel: MiningModelType;
  MiningSchema: MiningSchemaType;
  minkowski: MinkowskiType;
  MissingValueWeights: MissingValueWeightsType;
  ModelExplanation: ModelExplanationType;
  ModelLiftGraph: ModelLiftGraphType;
  ModelStats: ModelStatsType;
  ModelVerification: ModelVerificationType;
  MultivariateStat: MultivariateStatType;
  MultivariateStats: MultivariateStatsType;
  NaiveBayesModel: NaiveBayesModelType;
  NearestNeighborModel: NearestNeighborModelType;
  NeuralInput: NeuralInputType;
  NeuralInputs: NeuralInputsType;
  NeuralLayer: NeuralLayerType;
  NeuralNetwork: NeuralNetworkType;
  NeuralOutput: NeuralOutputType;
  NeuralOutputs: NeuralOutputsType;
  Neuron: NeuronType;
  Node: NodeType;
  NonseasonalComponent: NonseasonalComponentType;
  NonseasonalFactor: NonseasonalFactorType;
  NormalDistributionForBN: NormalDistributionForBNType;
  NormalizedCountTable: COUNTTABLETYPE;
  NormContinuous: NormContinuousType;
  NormDiscrete: NormDiscreteType;
  Numerator: NumeratorType;
  NumericInfo: NumericInfoType;
  NumericPredictor: NumericPredictorType;
  OptimumLiftGraph: OptimumLiftGraphType;
  OutlierEffect: OutlierEffectType;
  Output: OutputType;
  OutputField: OutputFieldType;
  PairCounts: PairCountsType;
  Parameter: ParameterType;
  ParameterField: ParameterFieldType;
  ParameterList: ParameterListType;
  ParamMatrix: ParamMatrixType;
  ParentValue: ParentValueType;
  Partition: PartitionType;
  PartitionFieldStats: PartitionFieldStatsType;
  PastVariances: PastVariancesType;
  PCell: PCellType;
  PCovCell: PCovCellType;
  PCovMatrix: PCovMatrixType;
  PMML: PMMLType;
  PoissonDistribution: PoissonDistributionType;
  PolynomialKernelType: PolynomialKernelTypeType;
  PPCell: PPCellType;
  PPMatrix: PPMatrixType;
  PredictiveModelQuality: PredictiveModelQualityType;
  Predictor: PredictorType;
  PredictorTerm: PredictorTermType;
  PsiVector: PsiVectorType;
  Quantile: QuantileType;
  RadialBasisKernel: RadialBasisKernelType;
  RadialBasisKernelType: RadialBasisKernelTypeType;
  RandomLiftGraph: RandomLiftGraphType;
  REALEntries: REALEntriesType;
  REALSparseArray: REALSparseArrayType;
  Regression: RegressionType;
  RegressionModel: RegressionModelType;
  RegressionTable: RegressionTableType;
  RegressorValues: RegressorValuesType;
  Residuals: ResidualsType;
  ResidualSquareCoefficients: ResidualSquareCoefficientsType;
  ResultField: ResultFieldType;
  ROC: ROCType;
  ROCGraph: ROCGraphType;
  row: any;
  RuleSelectionMethod: RuleSelectionMethodType;
  RuleSet: RuleSetType;
  RuleSetModel: RuleSetModelType;
  Scorecard: ScorecardType;
  ScoreDistribution: ScoreDistributionType;
  SeasonalComponent: SeasonalComponentType;
  SeasonalFactor: SeasonalFactorType;
  Seasonality_ExpoSmooth: Seasonality_ExpoSmoothType;
  Segment: SegmentType;
  Segmentation: SegmentationType;
  Sequence: SequenceType;
  SequenceModel: SequenceModelType;
  SequenceReference: SequenceReferenceType;
  SequenceRule: SequenceRuleType;
  SetPredicate: SetPredicateType;
  SetReference: SetReferenceType;
  SigmoidKernelType: SigmoidKernelTypeType;
  simpleMatching: SimpleMatchingType;
  SimplePredicate: SimplePredicateType;
  SimpleRule: SimpleRuleType;
  SimpleSetPredicate: SimpleSetPredicateType;
  squaredEuclidean: SquaredEuclideanType;
  StateSpaceModel: StateSpaceModelType;
  StateVector: StateVectorType;
  SupportVector: SupportVectorType;
  SupportVectorMachine: SupportVectorMachineType;
  SupportVectorMachineModel: SupportVectorMachineModelType;
  SupportVectors: SupportVectorsType;
  TableLocator: TableLocatorType;
  tanimoto: TanimotoType;
  Target: TargetType;
  Targets: TargetsType;
  TargetValue: TargetValueType;
  TargetValueCount: TargetValueCountType;
  TargetValueCounts: TargetValueCountsType;
  TargetValueStat: TargetValueStatType;
  TargetValueStats: TargetValueStatsType;
  Taxonomy: TaxonomyType;
  TestDistributions: TestDistributionsType;
  TextCorpus: TextCorpusType;
  TextDictionary: TextDictionaryType;
  TextDocument: TextDocumentType;
  TextIndex: TextIndexType;
  TextIndexNormalization: TextIndexNormalizationType;
  TextModel: TextModelType;
  TextModelNormalization: TextModelNormalizationType;
  TextModelSimiliarity: TextModelSimiliarityType;
  Theta: ThetaType;
  ThetaRecursionState: ThetaRecursionStateType;
  Time: TimeType;
  TimeAnchor: TimeAnchorType;
  TimeCycle: TimeCycleType;
  TimeException: TimeExceptionType;
  TimeSeries: TimeSeriesType;
  TimeSeriesModel: TimeSeriesModelType;
  Timestamp: TimestampType;
  TimeValue: TimeValueType;
  TrainingInstances: TrainingInstancesType;
  TransferFunctionValues: TransferFunctionValuesType;
  TransformationDictionary: TransformationDictionaryType;
  TransitionMatrix: TransitionMatrixType;
  TreeModel: TreeModelType;
  Trend_ExpoSmooth: Trend_ExpoSmoothType;
  TrendCoefficients: TrendCoefficientsType;
  TriangularDistributionForBN: TriangularDistributionForBNType;
  True: TrueType;
  UniformDistribution: UniformDistributionType;
  UniformDistributionForBN: UniformDistributionForBNType;
  UnivariateStats: UnivariateStatsType;
  Upper: UpperType;
  Value: ValueType;
  ValueProbability: ValueProbabilityType;
  VariableWeight: VariableWeightType;
  Variance: VarianceType;
  VarianceCoefficients: VarianceCoefficientsType;
  VectorDictionary: VectorDictionaryType;
  VectorFields: VectorFieldsType;
  VectorInstance: VectorInstanceType;
  VerificationField: VerificationFieldType;
  VerificationFields: VerificationFieldsType;
  XCoordinates: XCoordinatesType;
  YCoordinates: YCoordinatesType;
}
export var document: document;
